@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.template.JteContext
@import edu.kit.hci.soli.template.PageSpec
@import gg.jte.Content

@param PageSpec page
@param JteContext context
@param Content content
@param LoginStateModel login

<!DOCTYPE html>
<html lang="${context.localize("code")}" data-theme="light">
    <head>
        @if(page.description() != null)
            <meta name="description" content="${page.description()}">
        @endif
        <title>${page.title()}</title>
        <meta name="viewport" content="width=device-width"/>
        <meta charset="UTF-8">
        <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daisyui@4.12.14/dist/full.min.css" />
        <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/tailwindcss/dist/tailwind.min.css" />
        <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css" />
        <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js"></script>
    </head>

    <body>

        <div class="navbar bg-blue-600 text-primary-content">
            <div class="drawer-content flex flex-col items-center justify-center">
                <!-- Page content here -->
                <label for="my-drawer-2" class="btn btn-primary drawer-button lg:hidden">
                    Open drawer
                </label>
            </div>
        </div>


        <div class="container">
            <div class="flex flex-row flex-wrap">
                <aside class="w-full sm:w-1/3 md:w-1/4">
                    <div class="sticky w-full">
                        <!-- navigation -->
                            <div class="drawer lg:drawer-open">
                                <input id="my-drawer-2" type="checkbox" class="drawer-toggle" />

                                <div class="drawer-side">
                                    <label for="my-drawer-2" aria-label="close sidebar" class="drawer-overlay"></label>
                                    <ul class="menu bg-base-200 text-base-content min-h-full w-80 p-4">
                                        <!-- Sidebar content here -->
                                        <div class="avatar placeholder">
                                            <div class="bg-neutral text-neutral-content w-20 rounded-full">
                                                <span class="text-3xl">D</span>

                                            </div>

                                        </div>
                                        <label>Welcome, ${login.name()}</label>

                                        <div class="divider"></div>

                                        @if(login.kind() == LoginStateModel.Kind.VISITOR)
                                            <li><a href="/login">Login</a></li>
                                        @else
                                            <li>
                                                <form action="/logout"
                                                      method="post">
                                                    <input type="hidden"
                                                           name="${login.csrfToken().getParameterName()}"
                                                           value="${login.csrfToken().getToken()}"/>
                                                    <input type="submit"
                                                           value="Logout"/>
                                                </form>
                                            </li>

                                        @endif

                                        <li><a href="/">Raumbuchung</a></li>
                                        <li><a href="/bookings">Meine Reservierungen</a></li>
                                    </ul>
                                </div>
                            </div>
                    </div>
                </aside>
                <main role="main" class="w-full sm:w-full md:w-3/4 pt-2 px-2">
                    ${content}
                </main>
            </div>
        </div>

    </body>
</html>
