/**
 * UI Controller for Photo Album Dashboard
 * Commit: "fix: restore missing helper functions and resolve ReferenceError"
 */

document.addEventListener('DOMContentLoaded', async () => {
    const gallery = document.getElementById('photo-gallery');
    const userDisplay = document.getElementById('display-username');
    const modal = document.getElementById('photo-modal');
    const modalImg = document.getElementById('full-photo');

    // Проверка наличия элементов перед инициализацией
    if (gallery) {
        initDashboard(gallery, userDisplay);
    }

    if (modal && modalImg) {
        modal.addEventListener('click', (e) => {
            if (e.target.id === 'photo-modal' || e.target.classList.contains('modal-close')) {
                modal.classList.remove('active');
                modalImg.classList.remove('full-size');
                document.body.style.overflow = 'auto';
            }
        });

        modalImg.addEventListener('click', (e) => {
            e.stopPropagation();
            modalImg.classList.toggle('full-size');
        });
    }
});

async function initDashboard(gallery, userDisplay) {
    try {
        renderSkeletons(gallery, 6);

        // Используем относительный путь к API
        const response = await fetch('api/data'); 
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        
        const data = await response.json();

        if (userDisplay && data.username) {
            userDisplay.textContent = `Welcome, ${data.username}`;
        }

        renderGallery(data.resource_data, gallery);

    } catch (error) {
        console.error('Dashboard error:', error);
        showErrorMessage(gallery, `Could not connect to service: ${error.message}`);
    }
}

// --- Вспомогательные функции (Helper Functions) ---

function renderGallery(photos, container) {
    container.innerHTML = ''; 
    if (!photos || !Array.isArray(photos) || photos.length === 0) {
        container.innerHTML = `<div class="empty-state" style="grid-column: 1/-1; text-align: center; padding: 40px;">
            <span style="font-size: 3rem;">📸</span><p>Your album is empty.</p></div>`;
        return;
    }
    photos.forEach(photo => {
        const card = createPhotoCard(photo);
        container.appendChild(card);
    });
}

function createPhotoCard(photo) {
    const card = document.createElement('div');
    card.className = 'photo-card';
    card.style.cursor = 'pointer';

    const title = photo.title || 'Untitled';
    const imageUrl = photo.url || 'https://via.placeholder.com/400x300';

    card.innerHTML = `
        <div class="img-wrapper">
            <img src="${imageUrl}" alt="${title}" onerror="this.src='https://via.placeholder.com/400x300';">
        </div>
        <div class="photo-card-info">
            <h3>${title}</h3>
            <p>${photo.date || 'Recently added'}</p>
        </div>
    `;

    card.addEventListener('click', () => {
        const modal = document.getElementById('photo-modal');
        const modalImg = document.getElementById('full-photo');
        const caption = document.getElementById('modal-caption');
        if (modal && modalImg) {
            modalImg.src = imageUrl;
            caption.textContent = title;
            modal.classList.add('active');
            document.body.style.overflow = 'hidden';
        }
    });

    return card;
}

function renderSkeletons(container, count) {
    container.innerHTML = '';
    for (let i = 0; i < count; i++) {
        const skeleton = document.createElement('div');
        skeleton.className = 'photo-card';
        skeleton.innerHTML = `
            <div class="loading-skeleton" style="height: 200px; background: #eee;"></div>
            <div style="padding: 20px;">
                <div class="loading-skeleton" style="height: 15px; background: #eee; width: 70%; margin-bottom: 10px;"></div>
                <div class="loading-skeleton" style="height: 10px; background: #eee; width: 40%;"></div>
            </div>
        `;
        container.appendChild(skeleton);
    }
}

function showErrorMessage(container, message) {
    container.innerHTML = `
        <div style="grid-column: 1/-1; color: #dc3545; text-align: center; padding: 20px; background: #fff5f5; border-radius: 10px; border: 1px solid #ffc1c1;">
            <strong>Error:</strong> ${message}
        </div>
    `;
}