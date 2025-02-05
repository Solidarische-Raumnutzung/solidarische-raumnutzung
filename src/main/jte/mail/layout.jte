@import edu.kit.hci.soli.config.template.JteContext
@import gg.jte.Content

@param String title
@param Content content
@param JteContext context
@param String css

<style>
    ${css}
</style>

<table class="center">
    <tr class="header">
        <td>
            ${title}
        </td>
    </tr>

    <tr class="body">
        <td>
            <img class="w-20" src="${context.getHostname()}favicon.svg" alt="Soli-Logo">
            ${content}
        </td>
    </tr>

    <tr class="footer">
        <td>
            ${context.localize("mail.footer.text")} <a href="${context.getHostname()}">${context.localize("mail.website.name")}</a>
        </td>
    </tr>
</table>
