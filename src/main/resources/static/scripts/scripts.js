const quotes = [
    "Učení má být takové, aby to, co se jim předkládá, chápali jako cenný dar a ne jako cosi povinného, co jim zkazit dobrou náladu. - Albert Einstein",
    "Učit se bez přemýšlení je zbytečné. Přemýšlet bez učení je nebezpečné. - Konfucius",
     "S pomocí knih se mnozí stávají učenými i mimo školy. Bez knih nebývá učený nikdo ani ve škole. - Jan Amos Komenský",
     "Učenci a vědci! Nebojte se létat! Žádný učený z nebe nespadne! - Vlasta Burian",
       "Žádný věk není příliš pozdní k učení. - Latinské přísloví",
       "Neučíme se pro školu, ale pro život. - Seneca",
       "Učení je poklad, který bude všude následovat svého majitele. – Čínské přísloví"

    ];

    function changeQuote() {
        const randomIndex = Math.floor(Math.random() * quotes.length);
        document.getElementById('quoteDisplay').innerText = quotes[randomIndex];
    }

    setInterval(changeQuote, 10000); // Change quote every 5 seconds
    window.onload = changeQuote; // Show the first quote on page load

// úprava linků
document.addEventListener("DOMContentLoaded", function () {
    const hash = window.location.hash; // Get the URL hash (e.g., #v-pills-kontakt)
    if (hash) {
        // Find the tab button and content associated with the hash
        const tabButton = document.querySelector(`button[data-bs-target="${hash}"]`);
        const tabContent = document.querySelector(hash);

        if (tabButton && tabContent) {
            // Deactivate the currently active tab
            const activeTabButton = document.querySelector('.nav-link.active');
            const activeTabContent = document.querySelector('.tab-pane.active');

            if (activeTabButton) activeTabButton.classList.remove('active');
            if (activeTabContent) activeTabContent.classList.remove('show', 'active');

            // Activate the target tab
            tabButton.classList.add('active');
            tabContent.classList.add('show', 'active');
        }
    }
});