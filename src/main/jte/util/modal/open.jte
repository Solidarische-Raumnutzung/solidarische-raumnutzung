@import gg.jte.Content

@param Content message
@param String id
@param String style = "btn btn-ghost"
@param String alternate
@param Content content

<noscript>
    <a href="${alternate}" class="${style}">${message}</a>
</noscript>

<a onclick="${id + ".showModal()"}" class="${style} needs-js">${message}</a>
<dialog id="${id}" class="modal">
    <div class="modal-box">
        ${content}
    </div>
</dialog>