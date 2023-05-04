package appBank.models;


import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "pessoa")
public class Pessoa {
	
	@Id
	@Column(name = "cpf", length = 14, nullable = false)
	private String cpf;
	
	@Column(name = "nome", length = 50, nullable = false)
	private String nome;
	
	@Column(name = "data_nascimento", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	public Pessoa(String cpf, String nome, Date dataNascimento) {
		super();
		this.cpf = cpf;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
	}
	
	public Pessoa() {
		
	}
	
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	@Override
	public String toString() {
		return "Pessoa [cpf=" + cpf + ", nome=" + nome + ", dataNascimento=" + dataNascimento  + "]";
	}
	
}
