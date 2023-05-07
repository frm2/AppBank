package appBank.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import appBank.enums.TransferenciaType;

@Entity
@Table(name = "transferencia")
public class Transferencia {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column
	private int id;
	
	@ManyToOne
	@JsonIgnore
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
	
	public Transferencia() {
		
	}
	
	public Transferencia(Conta conta, TransferenciaType transferenciaType, BigDecimal valor, Date data) {
		super();
		this.conta = conta;
		this.transferenciaType = transferenciaType;
		this.valor = valor;
		this.data = data;
	}

	@Override
	public String toString() {
		return "Transferencia [id=" + id + ", contaId=" + conta.getId() + 
				", tipo=" + transferenciaType.getTypeValue() + ", data =" + data.toString() + 
				", valor=" + valor + "]";
	}
	
}
