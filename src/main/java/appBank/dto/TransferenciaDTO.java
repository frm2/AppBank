package appBank.dto;

import java.math.BigDecimal;

public class TransferenciaDTO {
    
    private Long idConta;
    
    private BigDecimal valor;

    public TransferenciaDTO(Long id, BigDecimal valor) {
        this.idConta = id;
        this.valor = valor;
    }

    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long id) {
        this.idConta = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
