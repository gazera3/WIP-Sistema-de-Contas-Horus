package com.sistema.service;

import com.sistema.model.Usuario;
import java.util.List;

public interface SuperAdminOperacoes {
    void removerAdmin(Long superAdminId, Long adminId);
    List<Usuario> listarTodosAdmins(Long superAdminId);
    void removerQualquerUsuario(Long superAdminId, Long usuarioId);
    List<Usuario> listarTodosUsuarios(Long superAdminId);
    void limparSistema(Long superAdminId);
    String gerarRelatorioCompleto(Long superAdminId);
    boolean isSuperAdmin(Long usuarioId);
}