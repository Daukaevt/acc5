document.addEventListener('DOMContentLoaded', async () => {
    const display = document.getElementById('user-display');
    const status = document.getElementById('status-text');
    
    try {
        // Спрашиваем свой же сервер (8086), а не Гейтвей
        const response = await fetch('/client/api/status');
        const data = await response.json();

        if (data.authenticated) {
            display.innerText = data.name;
            status.innerText = 'Авторизован';
            // Можно скрыть ссылку на логин или поменять её
        } else {
            display.innerText = 'ВХОД';
            status.innerText = 'Гость';
        }
    } catch (e) {
        console.error("Ошибка визуала:", e);
    }

    // Твои визуальные эффекты
    display.style.opacity = '1';
    display.style.transform = 'translateY(0)';
});