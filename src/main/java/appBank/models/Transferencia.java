package appBank.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

import appBank.enums.TransferenciaType;

@Entity
@Table(name = "transferencia")
public class Transferencia {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "conta_id", nullable=false)
	private Conta conta;
	
	@Column(name = "tipo_transferencia", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private TransferenciaType transferenciaType;

	@Column(nullable = false)
	private BigDecimal valor;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date data;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public TransferenciaType getTransferenciaType() {
		return transferenciaType;
	}

	public void setTransferenciaType(TransferenciaType transferenciaType) {
		this.transferenciaType = transferenciaType;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Transferencia [id=" + id + ", contaId=" + conta.getId() + 
				", tipo=" + transferenciaType.getTypeValue() + ", data =" + data.toString() + "]";
	}
	
}
