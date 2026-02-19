/**
 * UI Controller for Photo Album Dashboard
 * Commit: "feat: implement fullscreen image preview and modular rendering logic"
 */

document.addEventListener('DOMContentLoaded', async () => {
    const gallery = document.getElementById('photo-gallery');
    const userDisplay = document.getElementById('display-username');
    const modal = document.getElementById('photo-modal');

    // Глобальный слушатель для закрытия модалки (по фону или крестику)
    if (modal) {
        modal.addEventListener('click', (e) => {
            // Закрываем, если кликнули не по самой картинке, а по фону или крестику
            if (e.target.id === 'photo-modal' || e.target.classList.contains('modal-close')) {
                modal.classList.remove('active');
                document.body.style.overflow = 'auto'; // Возвращаем скролл
            }
        });
    }

    // Инициализация данных
    initDashboard(gallery, userDisplay);
});

async function initDashboard(gallery, userDisplay) {
    try {
        renderSkeletons(gallery, 6);

        const response = await fetch('/client/api/data');
        if (!response.ok) throw new Error('Network response was not ok');
        
        const data = await response.json();

        updateUserPill(data.username, userDisplay);
        renderGallery(data.resource_data, gallery);

    } catch (error) {
        console.error('Error fetching dashboard data:', error);
        showErrorMessage(gallery, 'Connection to Resource Service failed');
    }
}

function updateUserPill(username, displayElement) {
    if (username && displayElement) {
        displayElement.textContent = `Welcome, ${username}`;
    }
}

function renderGallery(photos, container) {
    container.innerHTML = ''; 

    if (!photos || !Array.isArray(photos) || photos.length === 0) {
        container.innerHTML = `
            <div class="empty-state" style="grid-column: 1/-1; text-align: center; padding: 60px;">
                <span style="font-size: 4rem; display: block; margin-bottom: 20px;">📸</span>
                <h2 style="color: var(--text-main);">No photos yet</h2>
                <p style="color: var(--text-muted);">Start building your collection today.</p>
            </div>`;
        return;
    }

    photos.forEach(photo => {
        container.appendChild(createPhotoCard(photo));
    });
}

function createPhotoCard(photo) {
    const card = document.createElement('div');
    card.className = 'photo-card';
    card.style.cursor = 'pointer';

    const title = photo.title || 'Untitled';
    const date = photo.date || 'Recently added';
    const imageUrl = photo.url || 'https://via.placeholder.com/400x300?text=No+Image';

    card.innerHTML = `
        <div class="img-wrapper">
            <img src="${imageUrl}" 
                 alt="${title}" 
                 onerror="this.onerror=null; this.src='https://via.placeholder.com/400x300?text=Image+Error';">
        </div>
        <div class="photo-card-info">
            <h3>${title}</h3>
            <p>${date}</p>
        </div>
    `;

    // Открытие модалки при клике
    card.addEventListener('click', () => {
        openModal(imageUrl, title);
    });

    return card;
}

function openModal(url, title) {
    const modal = document.getElementById('photo-modal');
    const modalImg = document.getElementById('full-photo');
    const caption = document.getElementById('modal-caption');

    if (modal && modalImg) {
        modalImg.src = url;
        caption.textContent = title;
        modal.classList.add('active');
        document.body.style.overflow = 'hidden'; // Запрещаем скролл фона
    }
}

function renderSkeletons(container, count) {
    container.innerHTML = '';
    for (let i = 0; i < count; i++) {
        const skeleton = document.createElement('div');
        skeleton.className = 'photo-card';
        skeleton.innerHTML = `
            <div class="loading-skeleton" style="height: 200px;"></div>
            <div style="padding: 20px;">
                <div class="loading-skeleton" style="height: 18px; width: 70%; margin-bottom: 10px; border-radius: 4px;"></div>
                <div class="loading-skeleton" style="height: 12px; width: 40%; border-radius: 4px;"></div>
            </div>
        `;
        container.appendChild(skeleton);
    }
}

function showErrorMessage(container, message) {
    container.innerHTML = `
        <div style="grid-column: 1/-1; color: #dc3545; text-align: center; padding: 30px; background: #fff5f5; border-radius: 20px; border: 1px dashed #ffc1c1;">
            <strong style="display: block; font-size: 1.2rem; margin-bottom: 5px;">Ops! Something went wrong</strong>
            <span>${message}</span>
        </div>
    `;
}