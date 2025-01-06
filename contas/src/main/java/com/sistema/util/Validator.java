package com.sistema.util;

import com.sistema.exception.SistemaException;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static void validarEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new SistemaException("Email inválido");
        }
    }

    public static void validarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            throw new SistemaException("CPF inválido");
        }

        try {
            Long.parseLong(cpf);
        } catch (NumberFormatException e) {
            throw new SistemaException("CPF deve conter apenas números");
        }
    }

    public static void validarCNPJ(String cnpj) {
        if (cnpj == null || cnpj.length() != 14) {
            throw new SistemaException("CNPJ inválido");
        }

        try {
            Long.parseLong(cnpj);
        } catch (NumberFormatException e) {
            throw new SistemaException("CNPJ deve conter apenas números");
        }
    }

    public static void validarDocumento(String documento) {
        if (documento == null) {
            throw new SistemaException("Documento não pode ser nulo");
        }

        documento = documento.replaceAll("[^0-9]", "");

        if (documento.length() == 11) {
            validarCPF(documento);
        } else if (documento.length() == 14) {
            validarCNPJ(documento);
        } else {
            throw new SistemaException("Documento inválido");
        }
    }

    public static void validarSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new SistemaException("Senha deve ter pelo menos 6 caracteres");
        }
    }
}