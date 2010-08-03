package der.ponto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author Marcius
 */
@Entity
@DiscriminatorValue("CANCELADO")
public class StatusCancelado extends StatusOcorrencia {

	public StatusCancelado() {
		this.setId(Short.valueOf("4"));
		this.setDescricao("Cancelado");
	}

    StatusCancelado(Ocorrencia ocorrencia) {
        this();
        this.ocorrencia = ocorrencia;
    }

    @Override
	public void autorizar() throws StatusException {
		throw new StatusException("Esta ocorrência já foi cancelada.("+ocorrencia.getId()+")");
	}

    @Override
	public void validar() throws StatusException {
		throw new StatusException("Esta ocorrência já foi cancelada.("+ocorrencia.getId()+")");
	}

    @Override
    public void cancelar() throws StatusException {
        throw new StatusException("Esta ocorrência já foi cancelada.("+ocorrencia.getId()+")");
    }

    @Override
    public void voltar() throws StatusException {
        throw new StatusException("Esta ocorrência já foi cancelada.("+ocorrencia.getId()+")");
    }

}