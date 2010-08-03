package der.ponto;

//TODO Refatorar para a classe Ocorrencias
public enum Motivo {
    A("Atestado"),
    E("Especial"),
	F("Férias"),
    J("Justificativa"),
    L("Licença Saúde"),
    O("Folga"),
    P("Particular"),
    R("Rota (Atraso)"),
    S("Serviço (Saída)"),
    T("Treinamento"),    
    V("Viagem a serviço");

	String valor;

	Motivo(String valor){
		this.valor = valor;
	}

	public String getValor(){
		return this.valor;
	}    
}
