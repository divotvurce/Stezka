/*!
* Start Bootstrap - Modern Business v5.0.7 (https://startbootstrap.com/template-overviews/modern-business)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-modern-business/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project
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
