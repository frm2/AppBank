package appBank.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "conta")
public class Conta {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column
	private long id;

	@ManyToOne
	@JoinColumn(name = "cpf_pessoa")
	private Pessoa pessoa;

	@Column
	private int agencia;

	@Column
	private boolean bloqueado;
	
	@OneToMany(mappedBy = "conta")
	private List<Transferencia> transferencias;

	public Conta(Pessoa pessoa, int agencia, boolean bloqueado) {
		super();
		this.pessoa = pessoa;
		this.agencia = agencia;
		this.bloqueado = bloqueado;
	}

	public Conta() {

	}

	public long getId() {
		return id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public int getAgencia() {
		return agencia;
	}

	public void setAgencia(int agencia) {
		this.agencia = agencia;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	public List<Transferencia> getTransferencias() {
		return transferencias;
	}

	public void setTransferencias(List<Transferencia> transferencias) {
		this.transferencias = transferencias;
	}

	@Override
	public String toString() {
		return "Conta [id=" + id + ", cpf_pessoa=" + pessoa.getCpf() + 
				", agencia=" + agencia + ", bloqueado=" + bloqueado + "]";
	}

}
