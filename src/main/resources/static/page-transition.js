(function () {

    "use strict";

    function createLoader() {

        if (document.getElementById("cityfix-page-loader")) {
            return document.getElementById("cityfix-page-loader");
        }

        const loader = document.createElement("div");

        loader.id = "cityfix-page-loader";

        document.body.prepend(loader);

        return loader;
    }


    function startLoading() {

        const loader = createLoader();

        loader.classList.remove("complete");

        loader.classList.add("loading");
    }


    function finishLoading() {

        const loader = document.getElementById("cityfix-page-loader");

        if (!loader) {
            return;
        }

        loader.classList.remove("loading");

        loader.classList.add("complete");
    }


    document.addEventListener("DOMContentLoaded", function () {

        const links = document.querySelectorAll("a[href]");

        links.forEach(function (link) {

            link.addEventListener("click", function (event) {

                const href = link.getAttribute("href");

                if (!href) {
                    return;
                }

                /*
                 * Ignore:
                 * - # links
                 * - javascript links
                 * - downloads
                 * - new tab
                 * - external links
                 * - Ctrl/Command click
                 */

                if (
                    href.startsWith("#") ||
                    href.startsWith("javascript:") ||
                    link.hasAttribute("download") ||
                    link.target === "_blank" ||
                    event.ctrlKey ||
                    event.metaKey ||
                    event.shiftKey ||
                    event.altKey
                ) {
                    return;
                }

                /*
                 * Only animate same-site navigation.
                 */

                try {

                    const target = new URL(
                        href,
                        window.location.href
                    );

                    if (target.origin !== window.location.origin) {
                        return;
                    }

                } catch (error) {
                    return;
                }

                startLoading();

            });

        });

    });


    window.addEventListener("load", function () {

        finishLoading();

    });


})();