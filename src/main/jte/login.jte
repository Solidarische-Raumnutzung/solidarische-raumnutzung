<!doctype html>
<html lang="en-us" data-theme="light">
<head>
    <meta charset="utf-8" />
    <title>Daisy UI Tailwind CSS Login Component</title>
    <link rel="stylesheet" href="https://unpkg.com/daisyui@latest/dist/full.css" />
    <script src="https://cdn.tailwindcss.com/3.4.3"></script>
</head>
<body>
<div class="flex justify-center">
    <div class="card w-96 bg-base-100 shadow-xl mt-20 mb-20">
        <div class="card-body">
            <h2 class="card-title">Login</h2>
            <a href="/oauth2/authorization/kit" class="btn btn-wide">KIT Login</a>

            <div class="collapse collapse-arrow bg-base-200">
                <input type="radio" name="my-accordion-2" />
                <div class="collapse-title text-xl font-medium">Admin</div>
                <div class="collapse-content">
                    <form name="f" action="/login" method="POST">
                        <div class="items-center mt-2">
                            <label class="input input-bordered flex items-center gap-2 mb-2">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" class="w-4 h-4 opacity-70">
                                    <path d="M2.5 3A1.5 1.5 0 0 0 1 4.5v.793c.026.009.051.02.076.032L7.674 8.51c.206.1.446.1.652 0l6.598-3.185A.755.755 0 0 1 15 5.293V4.5A1.5 1.5 0 0 0 13.5 3h-11Z" />
                                    <path d="M15 6.954 8.978 9.86a2.25 2.25 0 0 1-1.956 0L1 6.954V11.5A1.5 1.5 0 0 0 2.5 13h11a1.5 1.5 0 0 0 1.5-1.5V6.954Z" />
                                </svg>
                                <input type="text" class="grow" id="username" name="username" placeholder="Username" />
                            </label>
                            <label class="input input-bordered flex items-center gap-2 mb-2">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" class="w-4 h-4 opacity-70">
                                    <path fill-rule="evenodd" d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z" clip-rule="evenodd" />
                                </svg>
                                <input id="password" name="password" type="password" class="grow" placeholder="Password" />
                            </label>
                        </div>
                        <div class="    card-actions justify-end">
                            <button class="btn btn-primary w-full" type="submit">Login</button>
                        </div>
                    </form>
                </div>
            </div>





        </div>
    </div>
</div>
</body>
</html>