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
            <div>
                <img class="w-20 rounded-full" src="${context.getHostname()}favicon_180x180.png" alt="Soli-Logo">
            </div>
            ${content}
        </td>
    </tr>

    <tr class="footer">
        <td>
            ${context.localize("mail.footer.text")} <a href="${context.getHostname()}">${context.localize("mail.website.name")}</a>
        </td>
    </tr>
</table>