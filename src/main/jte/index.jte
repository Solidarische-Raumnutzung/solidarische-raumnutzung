@import edu.kit.hci.soli.DemoModel
@import edu.kit.hci.soli.template.JteContext
@import edu.kit.hci.soli.template.PageSpec

@param DemoModel model
@param JteContext context

@template.layout(page = new PageSpec("Index", "Soli"), context = context, content = @`
    <div class="hero bg-base-200 min-h-screen">
        <div class="hero-content text-center">
            <div class="max-w-md">
                <h1 class="text-5xl font-bold">${context.localize("index.hello", model.name)}</h1>
                <p class="py-6">
                    ${context.localize("index.count", model.visits)}
                </p>
            </div>
        </div>
    </div>
`)
