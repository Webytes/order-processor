package kinman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiKeyHandlerInterceptor extends HandlerInterceptorAdapter {

    AccountRepository accountRepository;

    public ApiKeyHandlerInterceptor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /*
     *  Ensure that there is a valid API key on the request. It can be in a header named "kinman-api-key" or
     *  on the query string in a parameter named "key".
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // check request header first -- it's preferred to set in header rather than expose key on query string.
        String apiKey = request.getHeader("kinman-api-key");

        if (apiKey == null) {
            // but, if you really hate setting headers, we'll let you use the query string.
            apiKey = request.getParameter("key");
        }

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
