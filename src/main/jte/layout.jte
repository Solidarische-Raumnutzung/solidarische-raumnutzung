@import edu.kit.hci.soli.template.JteContext
@import edu.kit.hci.soli.template.PageSpec
@import gg.jte.Content;

@param PageSpec page
@param JteContext context
@param Content content

<html lang="${context.localize("code")}">
    <head>
        @if(page.description() != null)
            <meta name="description" content="${page.description()}">
        @endif
        <title>${page.title()}</title>
        <meta name="viewport" content="width=device-width"/>
        <link href="https://cdn.jsdelivr.net/npm/daisyui@4.12.14/dist/full.min.css" rel="stylesheet" type="text/css" />
        <script src="https://cdn.tailwindcss.com"></script>
        <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js"></script>
    </head>
    <body>
        <main class="flex flex-col justify-center items-center p-4 h-screen">
            ${content}
        </main>
    </body>
</html>
