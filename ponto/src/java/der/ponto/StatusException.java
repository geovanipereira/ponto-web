package der.ponto;

import der.DerException;

/**
 * Exceção de consistência do Status da Ocorrência
 * @author Marcius
 *
 */
public class StatusException extends DerException {

    private static final long serialVersionUID = 1L;

    public StatusException(String mensagem) {
        super(mensagem);
    }
}
