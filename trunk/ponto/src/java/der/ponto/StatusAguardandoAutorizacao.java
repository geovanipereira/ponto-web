package der.ponto;

/**
 *
 * @author Marcius
 */
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("AGUARDANDO_AUTORIZACAO")
public class StatusAguardandoAutorizacao extends StatusOcorrencia {

    public StatusAguardandoAutorizacao() {
        this.setId(Short.valueOf("1"));
        this.setDescricao("Aguardando Autorização");
    }

    public StatusAguardandoAutorizacao(Ocorrencia ocorrencia) {
        this();
        this.ocorrencia = ocorrencia;
    }

    @Override
    public void autorizar() throws StatusException {
        StatusOcorrencia aguardandoValidacao = new StatusAguardandoValidacao(ocorrencia);
        ocorrencia.setStatus(aguardandoValidacao);
    }

    @Override
    public void validar() throws StatusException {
        throw new StatusException("Esta ocorrência não pode ser validada.("+ocorrencia.getId()+")");
    }

    @Override
    public void cancelar() throws StatusException {
        StatusOcorrencia cancelado = new StatusCancelado(ocorrencia);
        ocorrencia.setStatus(cancelado);
    }

    @Override
    public void voltar() throws StatusException {
        throw new StatusException("Esta ocorrência ainda não foi autorizada.("+ocorrencia.getId()+")");
    }
}
