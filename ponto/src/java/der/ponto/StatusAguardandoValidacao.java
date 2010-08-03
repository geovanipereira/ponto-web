package der.ponto;

/**
 *
 * @author 39291
 */
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("AGUARDANDO_VALIDACAO")
public class StatusAguardandoValidacao extends StatusOcorrencia {

    public StatusAguardandoValidacao() {
		this.setId(Short.valueOf("2"));
		this.setDescricao("Aguardando Validação");
	}

	public StatusAguardandoValidacao(Ocorrencia ocorrencia) {
		this();
		this.ocorrencia = ocorrencia;
	}

    @Override
	public void autorizar() throws StatusException {
		throw new StatusException("Esta ocorrência já foi autorizada.("+ocorrencia.getId()+")");
	}

    @Override
	public void validar() throws StatusException {
		StatusOcorrencia validado = new StatusValidado();
		ocorrencia.setStatus(validado);
	}

    @Override
    public void cancelar() throws StatusException {
        throw new StatusException("Esta ocorrência já foi autorizada.("+ocorrencia.getId()+")");
    }

    @Override
    public void voltar() throws StatusException {
        StatusOcorrencia aguardandoAutorizacao = new StatusAguardandoAutorizacao();
		ocorrencia.setStatus(aguardandoAutorizacao);
    }

}
