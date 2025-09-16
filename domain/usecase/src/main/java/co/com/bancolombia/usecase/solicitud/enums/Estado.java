package co.com.bancolombia.usecase.solicitud.enums;

public enum Estado {
    PENDIENTE_DE_REREVISION(1),
    RECHAZADA(2),
    REVISION_MANUAL(3),
    APROVADA(4);

    public final Integer valor;

    Estado(Integer valor) {
        this.valor = valor;
    }
}
