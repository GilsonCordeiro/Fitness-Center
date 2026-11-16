package Acadmy.start.project.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionConstants {

    public static final String ERRO_ENTIDADE_NAO_ENCONTRADA = "Nao foi possivel encontrar %s com o id %s.";
    public static final String ERRO_AO_SALVAR_ENTIDADE = "Erro ao salvar %s. Tente novamente mais tarde.";
    public static final String ERRO_AO_BUSCAR_ENTIDADE = "Erro ao buscar %s. Tente novamente mais tarde.";
    public static final String ERRO_AO_ATUALIZAR_ENTIDADE = "Erro ao atualizar %s. Tente novamente mais tarde.";
    public static final String ERRO_AO_LISTAR_TODOS = "Erro ao listar %s. Tente novamente mais tarde.";
    public static final String ERRO_AO_EXCLUIR_ENTIDADE = "Erro ao excluir %s. Tente novamente mais tarde.";
    public static final String ERRO_AO_FILTRAR_ENTIDADE = "Erro ao filtrar %s. Tente novamente mais tarde.";
}