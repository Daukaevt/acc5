/**
 * Управление авторизацией и анимация интерфейса
 */
document.addEventListener('DOMContentLoaded', () => {
    // 1. Конфигурация
    const GATEWAY_URL = 'http://localhost:8080';
    const savedUser = localStorage.getItem('username');
    
    // 2. Поиск элементов
    const display = document.getElementById('user-display');
    const status = document.getElementById('status-text');
    const link = document.getElementById('auth-link');

    // 3. Логика отображения и ссылок
    if (savedUser && savedUser !== 'null') {
        display.innerText = savedUser;
        status.innerText = `Авторизован (${savedUser})`;
        // Ведем в личный альбом через шлюз
        link.href = `${GATEWAY_URL}/hello/api/users/my`;
    } else {
        display.innerText = 'ВХОД';
        status.innerText = 'Гость';
        // Ведем на форму логина через шлюз
        link.href = `${GATEWAY_URL}/auth/login`;
    }

    // 4. Анимация появления (через JS-стили)
    display.style.opacity = '0';
    display.style.transition = 'opacity 1.5s ease-in-out, transform 1s ease-out';
    display.style.transform = 'translateY(10px)';

    // Запускаем анимацию с небольшой задержкой
    setTimeout(() => {
        display.style.opacity = '1';
        display.style.transform = 'translateY(0)';
    }, 300);
});