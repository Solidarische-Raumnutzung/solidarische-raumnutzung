@import gg.jte.Content

@param Content message
@param String id
@param String style = "btn"

@if (id.startsWith("modal_")) <%-- We have a modal ID --%>
    <a onclick="${id + ".close()"}" class="${style}">${message}</a>
@else <%-- We have a link --%>
    <a href="${id}" class="${style}">${message}</a>
@endif
