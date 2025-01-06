package com.sistema;

import com.sistema.model.Usuario;
import com.sistema.service.UsuarioService;
import com.sistema.exception.SistemaException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner run(UsuarioService service) {
        return args -> {
            try {
                // Criar super admin
                System.out.println("Criando Super Admin...");
                Usuario superAdmin = service.criarSuperAdmin(
                        "superadmin",
                        "Super Admin",
                        "super@admin.com",
                        "12345678901",
                        "senha123"
                );
                System.out.println("Super Admin criado com sucesso! ID: " + superAdmin.getId());

                // Criar admins
                System.out.println("\nCriando Admins...");
                Usuario admin1 = service.criarAdmin(
                        "admin1",
                        "Admin Um",
                        "admin1@email.com",
                        "98765432101",
                        "senha123",
                        superAdmin.getId(),
                        5
                );

                Usuario admin2 = service.criarAdmin(
                        "admin2",
                        "Admin Dois",
                        "admin2@email.com",
                        "98765432102",
                        "senha123",
                        superAdmin.getId(),
                        3
                );

                // Criar usuários comuns
                System.out.println("\nCriando Usuários...");
                Usuario user1 = service.criarUsuarioComum(
                        "user1",
                        "Usuario Um",
                        "user1@email.com",
                        "11122233344",
                        "senha123",
                        admin1.getId()
                );

                Usuario user2 = service.criarUsuarioComum(
                        "user2",
                        "Usuario Dois",
                        "user2@email.com",
                        "22233344455",
                        "senha123",
                        admin1.getId()
                );

                Usuario user3 = service.criarUsuarioComum(
                        "user3",
                        "Usuario Três",
                        "user3@email.com",
                        "33344455566",
                        "senha123",
                        admin2.getId()
                );

                // Listar admins
                System.out.println("\nListando todos os administradores:");
                List<Usuario> admins = service.listarTodosAdmins(superAdmin.getId());
                admins.forEach(admin -> System.out.println("- " + admin.getNome()));

                // Gerar relatório
                System.out.println("\nGerando relatório completo:");
                String relatorio = service.gerarRelatorioCompleto(superAdmin.getId());
                System.out.println(relatorio);

                // Remover um admin e seus usuários
                System.out.println("\nRemovendo Admin Dois e seus usuários...");
                service.removerAdmin(superAdmin.getId(), admin2.getId());

                // Listar usuários após remoção
                System.out.println("\nListando todos os usuários após remoção:");
                List<Usuario> usuarios = service.listarTodosUsuarios(superAdmin.getId());
                usuarios.forEach(user -> System.out.println("- " + user.getNome() +
                        " (" + user.getTipo() + ")"));

            } catch (SistemaException e) {
                System.err.println("\nErro no sistema: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("\nErro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}