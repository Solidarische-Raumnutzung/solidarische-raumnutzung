@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import gg.jte.Content
@import static org.springframework.http.HttpMethod.*

@param PageSpec page
@param LayoutParams layout
@param JteContext context

@param Content content

<!DOCTYPE html>
<html lang="${context.localize("code")}" data-theme="light">
<head>
    @if(page.description() != null)
        <meta name="description" content="${page.description()}">
    @endif
    <title>${page.title()}</title>
    <meta name="viewport" content="width=device-width"/>
    <meta charset="UTF-8">

    <link rel="icon" href="/favicon.svg">
    <link rel="mask-icon" href="/mask-icon.svg" color="#000000">
    <link rel="apple-touch-icon" href="/favicon_180x180.png">
    <link rel="manifest" href="/manifest.json">

    <link rel="icon" type="image/x-type" sizes="16x16" href="/favicon.ico">

    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daisyui@4.12.14/dist/full.min.css"/>
    <!-- TODO: this tailwind version is incredibly old. We should probably upgrade (but just replacing the version breaks the UI) -->
    <link rel="stylesheet" type="text/css"
          href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css"/>
    <link rel="stylesheet" type="text/css"
          href="https://solidarische-raumnutzung.github.io/tailwind-typography-stylesheet/style.css">
    <link rel="stylesheet" type="text/css" href="/soli.css">
    <style>
        :root {
            --soli-priority-lowest: "p-${context.localize("bookings.priority.LOWEST")}";
            --soli-priority-medium: "p-${context.localize("bookings.priority.MEDIUM")}";
            --soli-priority-highest: "p-${context.localize("bookings.priority.HIGHEST")}";
            --soli-booking-own: "${context.localize("bookings.own")}";
        }
    </style>
    <noscript>
        <style>
            .needs-js {
                display: none;
            }
        </style>
    </noscript>
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js"></script>
</head>

<body>


<div class="flex flex-row flex-wrap ">

    <div class="sticky w-full">

        <div class="drawer lg:drawer-open">
            <input id="my-drawer-2" type="checkbox" class="drawer-toggle"/>
            <div class="drawer-content w-full md:w-full">
                <div class="navbar bg-blue-600 text-primary-content">
                    <div class="drawer-content flex flex-col items-center justify-center">
                        <!-- Page content here -->
                        <label for="my-drawer-2" class="btn btn-primary drawer-button lg:hidden">
                            Open drawer
                        </label>
                    </div>
                </div>
                <div class="p-4">
                    <div class="card bg-base-100 w-full shadow-xl p-4">
                        <div class="card-body">
                            ${content}
                        </div>
                    </div>
                </div>


                <!-- Page content here -->


            </div>
            <div class="drawer-side flex shadow-xl">
                <label for="my-drawer-2" aria-label="close sidebar" class="drawer-overlay"></label>
                <div class="min-h-screen bg-base-200">
                    <ul class="menu bg-base-200 text-base-content min-h-80 w-80 p-4">
                        <!-- Sidebar content here -->

                        <div class="flex flex-row gap-3"    >
                            <li>
                                <div class="basis-1/4"><a alt="main" class="avatar placeholder" href="/">
                                        <div class="w-20 rounded-full">
                                            <img src="/favicon.svg">
                                        </div>
                                    </a>
                                </div>
                            </li>
                            <div class="basis-1/4">
                                <div class="relative h-20 w-20">
                                    <div class="absolute inset-x-0 bottom-0 h-16">
                                        <label>Welcome, ${layout.getLogin().name()}</label>
                                    </div>
                                </div>
                            </div>
                            <div class="basis-1/2">
                                <div class="relative h-20 w-20">
                                    <div class="absolute inset-x-0 bottom-0 h-16">
                                        @if (layout.getLogin().kind() == LoginStateModel.Kind.VISITOR)
                                            <a href="/login" aria-label="${context.localize("login.login")}"
                                               class="btn btn-outline btn-primary">
                                                <i class="fa-solid fa-right-to-bracket"></i>
                                            </a>
                                        @else
                                            @template.util.form(csrf = layout.getLogin().csrfToken(), action = "/logout", method = POST, content = @`
                                                <button class="btn btn-outline btn-primary"
                                                        aria-label="${context.localize("login.logout")}" type="submit">
                                                    <i class="fa-solid fa-right-from-bracket"></i>
                                                </button>
                                            `)
                                        @endif
                                    </div>
                                </div>
                            </div>
                        </div>

                        @if(layout.getLogin().kind() == LoginStateModel.Kind.ADMIN)
                            <div class="divider"></div>

                            <li><a href="/admin/users"><i
                                            class="fa-solid fa-list"></i> ${context.localize("administration.user")}</a>
                            </li>
                            <li><a href="/admin/statistics"><i
                                            class="fa-solid fa-chart-line"></i> ${context.localize("administration.statistics")}
                                </a></li>
                        @endif

                        <div class="divider"></div>

                        @if (layout.getRoom() != null)
                            <li><a href="/${layout.getRoom().getId()}"><i
                                            class="fa-solid fa-calendar-days"></i> ${context.localize("calendar")}</a>
                            </li>
                            <li><a href="/${layout.getRoom().getId()}/bookings"><i
                                            class="fa-solid fa-list"></i> ${context.localize("bookings.my_bookings")}
                                </a>
                            </li>

                            <div class="divider"></div>

                            <li><a class="btn btn-outline btn-primary" href="/${layout.getRoom().getId()}/bookings/new"><i
                                            class="fa-solid fa-plus"></i> ${context.localize("bookings.new")}</a></li>
                        @endif

                    </ul>
                    <footer class="footer bg-base-300 text-base-content p-10 footer bg-neutral text-neutral-content items-center p-4 fixed bottom-0 w-full">
                        <nav>
                            <h6 class="footer-title">Services</h6>
                            <a class="link link-hover">Branding</a>
                            <a class="link link-hover">Design</a>
                            <a class="link link-hover">Marketing</a>
                            <a class="link link-hover">Advertisement</a>
                        </nav>
                        <nav>
                            <h6 class="footer-title">Company</h6>
                            <a class="link link-hover">About us</a>
                            <a class="link link-hover">Contact</a>
                            <a class="link link-hover">Jobs</a>
                            <a class="link link-hover">Press kit</a>
                        </nav>
                        <nav>
                            <h6 class="footer-title">Social</h6>
                            <div class="grid grid-flow-col gap-4">
                                <a>
                                    <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            width="24"
                                            height="24"
                                            viewBox="0 0 24 24"
                                            class="fill-current">
                                        <path
                                                d="M24 4.557c-.883.392-1.832.656-2.828.775 1.017-.609 1.798-1.574 2.165-2.724-.951.564-2.005.974-3.127 1.195-.897-.957-2.178-1.555-3.594-1.555-3.179 0-5.515 2.966-4.797 6.045-4.091-.205-7.719-2.165-10.148-5.144-1.29 2.213-.669 5.108 1.523 6.574-.806-.026-1.566-.247-2.229-.616-.054 2.281 1.581 4.415 3.949 4.89-.693.188-1.452.232-2.224.084.626 1.956 2.444 3.379 4.6 3.419-2.07 1.623-4.678 2.348-7.29 2.04 2.179 1.397 4.768 2.212 7.548 2.212 9.142 0 14.307-7.721 13.995-14.646.962-.695 1.797-1.562 2.457-2.549z"></path>
                                    </svg>
                                </a>
                                <a>
                                    <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            width="24"
                                            height="24"
                                            viewBox="0 0 24 24"
                                            class="fill-current">
                                        <path
                                                d="M19.615 3.184c-3.604-.246-11.631-.245-15.23 0-3.897.266-4.356 2.62-4.385 8.816.029 6.185.484 8.549 4.385 8.816 3.6.245 11.626.246 15.23 0 3.897-.266 4.356-2.62 4.385-8.816-.029-6.185-.484-8.549-4.385-8.816zm-10.615 12.816v-8l8 3.993-8 4.007z"></path>
                                    </svg>
                                </a>
                                <a>
                                    <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            width="24"
                                            height="24"
                                            viewBox="0 0 24 24"
                                            class="fill-current">
                                        <path
                                                d="M9 8h-3v4h3v12h5v-12h3.642l.358-4h-4v-1.667c0-.955.192-1.333 1.115-1.333h2.885v-5h-3.808c-3.596 0-5.192 1.583-5.192 4.615v3.385z"></path>
                                    </svg>
                                </a>
                            </div>
                        </nav>
                    </footer>

                </div>

            </div>
        </div>
    </div>


</div>


<link rel="stylesheet" type="text/css"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css"/>
</body>
</html>
