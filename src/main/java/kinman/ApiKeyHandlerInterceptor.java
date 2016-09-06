package kinman;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiKeyHandlerInterceptor extends HandlerInterceptorAdapter {

    AccountRepository accountRepository;

    public ApiKeyHandlerInterceptor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /*
     *  Ensure that there is a valid API key on the request. It must be in a custom header named "kinman-api-key".
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiKey = request.getHeader("kinman-api-key");

        if (apiKey == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Account account = accountRepository.findByApiKey(apiKey);

        if (account == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        request.setAttribute("account", account);
        return true;
    }

}
