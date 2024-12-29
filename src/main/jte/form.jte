@import gg.jte.Content
@import org.springframework.http.HttpMethod
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param CsrfToken csrf
@param String cssClass
@param String action
@param HttpMethod method
@param Content content

<form name="f" action="${action}" method="${method == GET || method == POST ? method.toString() : "POST"}" class="${cssClass}">
    @if (csrf != null)
        <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
    @endif
    @if (method != GET && method != POST)
        <input type="hidden" name="_method" value="${method.toString()}"/>
    @endif
    ${content}
</form>