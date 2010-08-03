package der.ponto;

/**
 *
 * @author Marcius
 */
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("VALIDADO")
public class StatusValidado extends StatusOcorrencia {

	public StatusValidado() {
		this.setId(Short.valueOf("3"));
		this.setDescricao("Validado");
	}

    StatusValidado(Ocorrencia ocorrencia) {
        this();
        this.ocorrencia = ocorrencia;
    }
    
    @Override
	public void autorizar() throws StatusException {
		throw new StatusException("Esta ocorrência já foi validada.("+ocorrencia.getId()+")");
	}

    @Override
	public void validar() throws StatusException {
		throw new StatusException("Este ocorrência já foi validada.("+ocorrencia.getId()+")");
	}

    @Override
    public void cancelar() throws StatusException {
        StatusOcorrencia cancelado = new StatusCancelado(ocorrencia);
        ocorrencia.setStatus(cancelado);
    }

    @Override
    public void voltar() throws StatusException {
        throw new StatusException("Este ocorrência já foi validada.("+ocorrencia.getId()+")");
    }

}