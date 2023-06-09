package appBank.models;


import java.sql.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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

	@OneToMany(mappedBy = "pessoa")
	private List<Conta> contas;

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
//	public void setCpf(String cpf) {
//		this.cpf = cpf;
//	}
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
	
	public List<Conta> getContas() {
		return contas;
	}

	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}

	public boolean isCPFValid() {

		String cpf_aux = cpf;
		String cpfRegex = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}";
		String numeroRepetido = "^(\\d)\\1{10}$";

		if (!cpf_aux.matches(cpfRegex))
			return false;

		cpf_aux = cpf_aux.replaceAll("[^0-9]", ""); // remove any non-digit characters
		if (cpf_aux.length() != 11) {
			return false; // CPF must have 11 digits
		}

		if (cpf_aux.matches(numeroRepetido))
			return(false);

		int[] digits = new int[11];
		for (int i = 0; i < 11; i++) {
			digits[i] = Integer.parseInt(cpf_aux.substring(i, i + 1));
		}
		int sum = 0;
		for (int i = 0; i < 9; i++) {
			sum += digits[i] * (10 - i);
		}
		int remainder = sum % 11;
		if (remainder < 2) {
			if (digits[9] != 0) {
				return false;
			}
		} else {
			if (digits[9] != 11 - remainder) {
				return false;
			}
		}
		sum = 0;
		for (int i = 0; i < 10; i++) {
			sum += digits[i] * (11 - i);
		}
		remainder = sum % 11;
		if (remainder < 2) {
			if (digits[10] != 0) {
				return false;
			}
		} else {
			if (digits[10] != 11 - remainder) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "Pessoa [cpf=" + cpf + ", nome=" + nome 
				+ ", dataNascimento=" + dataNascimento  + "]";
	}

}
