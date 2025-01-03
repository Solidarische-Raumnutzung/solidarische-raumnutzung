@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import gg.jte.Content
@import static org.springframework.http.HttpMethod.*

@param LoginStateModel login
@param JteContext context

@param PageSpec page
@param Room room

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
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/tailwindcss/dist/tailwind.min.css"/>
    <style>
        :root {
            --tw-bg-opacity: 1;
            --tw-text-opacity: 1;
        }

        :root {
            color-scheme: light;
            --in: 72.06% 0.191 231.6;
            --su: 64.8% 0.150 160;
            --wa: 84.71% 0.199 83.87;
            --er: 71.76% 0.221 22.18;
            --pc: 15.2344% 0.017892 200.026556;
            --sc: 15.787% 0.020249 356.29965;
            --ac: 15.8762% 0.029206 78.618794;
            --nc: 84.7148% 0.013247 313.189598;
            --inc: 0% 0 0;
            --suc: 0% 0 0;
            --wac: 0% 0 0;
            --erc: 0% 0 0;
            --rounded-box: 1rem;
            --rounded-badge: 1.9rem;
            --animation-btn: 0.25s;
            --animation-input: .2s;
            --btn-focus-scale: 0.95;
            --border-btn: 1px;
            --p: 76.172% 0.089459 200.026556;
            --s: 78.9351% 0.101246 356.29965;
            --a: 79.3811% 0.146032 78.618794;
            --n: 23.5742% 0.066235 313.189598;
            --b1: 97.7882% 0.00418 56.375637;
            --b2: 93.9822% 0.007638 61.449292;
            --b3: 91.5861% 0.006811 53.440502;
            --bc: 23.5742% 0.066235 313.189598;
            --rounded-btn: 1.9rem;
            --tab-border: 2px;
            --tab-radius: 0.7rem;
        }

        @media (prefers-color-scheme: dark) {
            :root {
                color-scheme: dark;
                --in: 72.06% 0.191 231.6;
                --su: 64.8% 0.150 160;
                --wa: 84.71% 0.199 83.87;
                --er: 71.76% 0.221 22.18;
                --pc: 13.138% 0.0392 275.75;
                --sc: 14.96% 0.052 342.55;
                --ac: 14.902% 0.0334 183.61;
                --inc: 0% 0 0;
                --suc: 0% 0 0;
                --wac: 0% 0 0;
                --erc: 0% 0 0;
                --rounded-box: 1rem;
                --rounded-btn: 0.5rem;
                --rounded-badge: 1.9rem;
                --animation-btn: 0.25s;
                --animation-input: .2s;
                --btn-focus-scale: 0.95;
                --border-btn: 1px;
                --tab-border: 1px;
                --tab-radius: 0.5rem;
                --p: 65.69% 0.196 275.75;
                --s: 74.8% 0.26 342.55;
                --a: 74.51% 0.167 183.61;
                --n: 31.3815% 0.021108 254.139175;
                --nc: 74.6477% 0.0216 264.435964;
                --b1: 25.3267% 0.015896 252.417568;
                --b2: 23.2607% 0.013807 253.100675;
                --b3: 21.1484% 0.01165 254.087939;
                --bc: 74.6477% 0.0216 264.435964;
            }
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
                    <input id="my-drawer-2" type="checkbox" class="drawer-toggle"/>

                    <div class="drawer-side">
                        <label for="my-drawer-2" aria-label="close sidebar" class="drawer-overlay"></label>
                        <ul class="menu bg-base-200 text-base-content min-h-full w-80 p-4">
                            <!-- Sidebar content here -->

                            <div class="flex flex-row gap-3">
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
                                            <label>Welcome, ${login.name()}</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="basis-1/2">
                                    <div class="relative h-20 w-20">
                                        <div class="absolute inset-x-0 bottom-0 h-16">
                                            @if (login.kind() == LoginStateModel.Kind.VISITOR)
                                                <a href="/login" aria-label="${context.localize("login.login")}" class="btn btn-outline btn-primary">
                                                    <i class="fa-solid fa-right-to-bracket"></i>
                                                </a>
                                            @else
                                                @template.util.form(csrf = login.csrfToken(), action = "/logout", method = POST, content = @`
                                                    <button class="btn btn-outline btn-primary" aria-label="${context.localize("login.logout")}" type="submit">
                                                        <i class="fa-solid fa-right-from-bracket"></i>
                                                    </button>
                                                `)
                                            @endif
                                        </div>
                                    </div>
                                </div>
                            </div>

                            @if(login.kind() == LoginStateModel.Kind.ADMIN)
                                <div class="divider"></div>

                                <li><a href="/admin/users"><i class="fa-solid fa-list"></i> ${context.localize("administration.user")}</a></li>
                            @endif

                            <div class="divider"></div>

                            @if (room != null)
                                <li><a href="/${room.getId()}"><i class="fa-solid fa-calendar-days"></i> ${context.localize("calendar")}</a></li>
                                <li><a href="/${room.getId()}/bookings"><i class="fa-solid fa-list"></i> ${context.localize("bookings.my_bookings")}</a>
                                </li>

                                <div class="divider"></div>

                                <li><a class="btn btn-outline btn-primary"
                                       href="/${room.getId()}/bookings/new"><i class="fa-solid fa-plus"></i> ${context.localize("bookings.new")}</a></li>
                            @endif
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
<link rel="stylesheet" type="text/css"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css"/>
</body>
</html>
