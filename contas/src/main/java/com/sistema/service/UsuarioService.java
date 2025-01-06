package com.sistema.service;

import com.sistema.model.AdminConfig;
import com.sistema.model.TipoUsuario;
import com.sistema.model.Usuario;
import com.sistema.exception.SistemaException;
import com.sistema.repository.UsuarioRepository;
import com.sistema.repository.AdminConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UsuarioService implements SuperAdminOperacoes {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AdminConfigRepository adminConfigRepository;

    @Transactional
    public Usuario criarSuperAdmin(String usuario, String nome, String email, String documento, String senha) {
        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new SistemaException("Nome de usuário já existe");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new SistemaException("Email já está em uso");
        }

        Usuario superAdmin = Usuario.builder()
                .usuario(usuario)
                .nome(nome)
                .email(email)
                .documento(documento)
                .senha(senha)
                .tipo(TipoUsuario.SUPER_ADMIN)
                .build();

        return usuarioRepository.save(superAdmin);
    }

    @Transactional
    public Usuario criarAdmin(String usuario, String nome, String email, String documento,
                              String senha, Long superAdminId, int maxConexoes) {
        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new SistemaException("Nome de usuário já existe");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new SistemaException("Email já está em uso");
        }

        Usuario superAdmin = usuarioRepository.findById(superAdminId)
                .orElseThrow(() -> new SistemaException("Super Admin não encontrado"));

        if (superAdmin.getTipo() != TipoUsuario.SUPER_ADMIN) {
            throw new SistemaException("Apenas Super Admin pode criar admins");
        }

        Usuario admin = Usuario.builder()
                .usuario(usuario)
                .nome(nome)
                .email(email)
                .documento(documento)
                .senha(senha)
                .tipo(TipoUsuario.ADMIN)
                .build();

        admin = usuarioRepository.save(admin);

        AdminConfig config = new AdminConfig();
        config.setAdmin(admin);
        config.setMaxConexoes(maxConexoes);
        adminConfigRepository.save(config);

        return admin;
    }

    @Transactional
    public Usuario criarUsuarioComum(String usuario, String nome, String email,
                                     String documento, String senha, Long adminId) {
        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new SistemaException("Nome de usuário já existe");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new SistemaException("Email já está em uso");
        }

        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new SistemaException("Admin não encontrado"));

        if (admin.getTipo() != TipoUsuario.ADMIN) {
            throw new SistemaException("Apenas Admin pode criar usuários comuns");
        }

        AdminConfig config = adminConfigRepository.findByAdmin(admin)
                .orElseThrow(() -> new SistemaException("Configuração de admin não encontrada"));

        long usuariosAtuais = usuarioRepository.countByAdmin(admin);
        if (usuariosAtuais >= config.getMaxConexoes()) {
            throw new SistemaException("Limite de usuários atingido");
        }

        Usuario novoUsuario = Usuario.builder()
                .usuario(usuario)
                .nome(nome)
                .email(email)
                .documento(documento)
                .senha(senha)
                .tipo(TipoUsuario.USUARIO_COMUM)
                .admin(admin)
                .build();

        return usuarioRepository.save(novoUsuario);
    }

    @Override
    @Transactional
    public void removerAdmin(Long superAdminId, Long adminId) {
        Usuario superAdmin = usuarioRepository.findById(superAdminId)
                .orElseThrow(() -> new SistemaException("Super Admin não encontrado"));

        if (superAdmin.getTipo() != TipoUsuario.SUPER_ADMIN) {
            throw new SistemaException("Apenas Super Admin pode remover admins");
        }

        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new SistemaException("Admin não encontrado"));

        if (admin.getTipo() != TipoUsuario.ADMIN) {
            throw new SistemaException("Usuário selecionado não é um admin");
        }

        // Remove usuários vinculados
        List<Usuario> usuariosVinculados = usuarioRepository.findByAdmin(admin);
        usuarioRepository.deleteAll(usuariosVinculados);

        // Remove config do admin
        adminConfigRepository.findByAdmin(admin)
                .ifPresent(config -> adminConfigRepository.delete(config));

        // Remove o admin
        usuarioRepository.delete(admin);
    }

    @Override
    public List<Usuario> listarTodosAdmins(Long superAdminId) {
        if (!isSuperAdmin(superAdminId)) {
            throw new SistemaException("Apenas Super Admin pode listar todos os administradores");
        }
        return usuarioRepository.findByTipo(TipoUsuario.ADMIN);
    }

    @Override
    public List<Usuario> listarTodosUsuarios(Long superAdminId) {
        if (!isSuperAdmin(superAdminId)) {
            throw new SistemaException("Apenas Super Admin pode listar todos os usuários");
        }
        return usuarioRepository.findAll();
    }

    @Override
    public void removerQualquerUsuario(Long superAdminId, Long usuarioId) {
        if (!isSuperAdmin(superAdminId)) {
            throw new SistemaException("Apenas Super Admin pode remover qualquer usuário");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new SistemaException("Usuário não encontrado"));

        if (usuario.getTipo() == TipoUsuario.SUPER_ADMIN) {
            throw new SistemaException("Não é possível remover um Super Admin");
        }

        if (usuario.getTipo() == TipoUsuario.ADMIN) {
            removerAdmin(superAdminId, usuarioId);
        } else {
            usuarioRepository.delete(usuario);
        }
    }

    @Override
    @Transactional
    public void limparSistema(Long superAdminId) {
        if (!isSuperAdmin(superAdminId)) {
            throw new SistemaException("Apenas Super Admin pode limpar o sistema");
        }

        Usuario superAdmin = usuarioRepository.findById(superAdminId)
                .orElseThrow(() -> new SistemaException("Super Admin não encontrado"));

        adminConfigRepository.deleteAll();
        usuarioRepository.deleteAll();
        usuarioRepository.save(superAdmin);
    }

    @Override
    public String gerarRelatorioCompleto(Long superAdminId) {
        if (!isSuperAdmin(superAdminId)) {
            throw new SistemaException("Apenas Super Admin pode gerar relatório");
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("=== RELATÓRIO COMPLETO DO SISTEMA ===\n\n");

        long totalUsuarios = usuarioRepository.count();
        long totalAdmins = usuarioRepository.findByTipo(TipoUsuario.ADMIN).size();

        relatorio.append("ESTATÍSTICAS GERAIS:\n");
        relatorio.append("Total de usuários: ").append(totalUsuarios).append("\n");
        relatorio.append("Total de administradores: ").append(totalAdmins).append("\n\n");

        relatorio.append("DETALHES DOS ADMINISTRADORES:\n");
        for (Usuario admin : usuarioRepository.findByTipo(TipoUsuario.ADMIN)) {
            AdminConfig config = adminConfigRepository.findByAdmin(admin)
                    .orElse(new AdminConfig());

            List<Usuario> usuariosDoAdmin = usuarioRepository.findByAdmin(admin);

            relatorio.append("\nAdmin: ").append(admin.getNome())
                    .append(" (").append(admin.getEmail()).append(")\n");
            relatorio.append("Limite de conexões: ").append(config.getMaxConexoes()).append("\n");
            relatorio.append("Usuários ativos: ").append(usuariosDoAdmin.size()).append("\n");

            if (!usuariosDoAdmin.isEmpty()) {
                relatorio.append("Lista de usuários:\n");
                for (Usuario usuario : usuariosDoAdmin) {
                    relatorio.append("- ").append(usuario.getNome())
                            .append(" (").append(usuario.getEmail()).append(")\n");
                }
            }
        }

        return relatorio.toString();
    }

    @Override
    public boolean isSuperAdmin(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getTipo() == TipoUsuario.SUPER_ADMIN)
                .orElse(false);
    }

    public Usuario validarLogin(String usuario, String senha) {
        return usuarioRepository.findByUsuario(usuario)
                .filter(u -> u.getSenha().equals(senha))
                .orElseThrow(() -> new SistemaException("Usuário ou senha inválidos"));
    }

    public void atualizarLimiteConexoes(Long superAdminId, Long adminId, int novoLimite) {
        if (!isSuperAdmin(superAdminId)) {
            throw new SistemaException("Apenas Super Admin pode alterar limites");
        }

        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new SistemaException("Admin não encontrado"));

        AdminConfig config = adminConfigRepository.findByAdmin(admin)
                .orElseThrow(() -> new SistemaException("Configuração de admin não encontrada"));

        config.setMaxConexoes(novoLimite);
        adminConfigRepository.save(config);
    }
}