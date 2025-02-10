@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.domain.User

@param JteContext context
@param String css

@param User user

@template.mail.layout(title = context.lookup("mail.user_banned.heading"), context = context, css = css, content = @`
    <p>
        ${context.localize("mail.user_banned.message", user.getUsername())}
    </p>
    <p>
        ${context.localize("mail.user_banned.last_phrase")}
    </p>
    <p>
        ${context.localize("mail.ending")}
    </p>
`)
