document.addEventListener('DOMContentLoaded', async () => {
    const gallery = document.getElementById('photo-gallery');
    const userDisplay = document.getElementById('display-username');

    try {
        // Вызываем наш контроллер на 8086, который делает Relay Token
        const response = await fetch('/client/api/data');
        const data = await response.json();

        // Отображаем имя пользователя
        if (data.username) {
            userDisplay.textContent = `Welcome, ${data.username}`;
        }

        // Рендерим карточки из resource_data
        if (data.resource_data && Array.isArray(data.resource_data)) {
            gallery.innerHTML = ''; // Очищаем лоадер

            data.resource_data.forEach(item => {
                const card = document.createElement('div');
                card.className = 'photo-card';
                
                // Используем поля из твоего будущего JSON (например url и title)
                card.innerHTML = `
                    <img src="${item.url || 'https://via.placeholder.com/400x300'}" alt="Photo">
                    <div class="photo-card-info">
                        <h3>${item.title || 'Untitled'}</h3>
                        <p>${item.date || 'August 2025'}</p>
                    </div>
                `;
                gallery.appendChild(card);
            });
        } else {
            gallery.innerHTML = '<div class="loading-state">No photos found.</div>';
        }

    } catch (error) {
        console.error('Error fetching dashboard data:', error);
        gallery.innerHTML = '<div class="loading-state" style="color: red;">Error connection to Resource Service</div>';
    }
});