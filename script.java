// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 1. 平滑滚动
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            document.querySelector(this.getAttribute('href')).scrollIntoView({
                behavior: 'smooth'
            });
        });
    });

    // 2. 搜索功能
    const searchInput = document.getElementById('search-input');
    const searchBtn = document.getElementById('search-btn');
    const termsTable = document.getElementById('terms-table');
    const phrasesTable = document.getElementById('phrases-table');
    const sentencesList = document.getElementById('sentences-list');

    // 搜索处理函数
    function handleSearch() {
        const searchTerm = searchInput.value.trim().toLowerCase();
        if (!searchTerm) {
            // 清空搜索时移除所有高亮
            removeHighlights();
            return;
        }

        // 移除之前的高亮
        removeHighlights();

        // 搜索术语表格
        searchTable(termsTable, searchTerm);
        // 搜索短语表格
        searchTable(phrasesTable, searchTerm);
        // 搜索句型列表
        searchSentences(sentencesList, searchTerm);

        // 滚动到第一个匹配项
        const firstHighlight = document.querySelector('.highlight');
        if (firstHighlight) {
            firstHighlight.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    }

    // 搜索表格内容
    function searchTable(table, term) {
        const rows = table.querySelectorAll('tbody tr');
        rows.forEach(row => {
            const cells = row.querySelectorAll('td');
            let hasMatch = false;
            cells.forEach(cell => {
                const text = cell.textContent.toLowerCase();
                if (text.includes(term)) {
                    // 高亮匹配文本
                    const regex = new RegExp(`(${term})`, 'gi');
                    cell.innerHTML = cell.textContent.replace(regex, '<span class="highlight">$1</span>');
                    hasMatch = true;
                }
            });
            if (hasMatch) {
                row.style.backgroundColor = '#fef7fb';
            }
        });
    }

    // 搜索句型内容
    function searchSentences(list, term) {
        const items = list.querySelectorAll('.sentence-item');
        items.forEach(item => {
            const content = item.querySelector('.sentence-content');
            const text = content.textContent.toLowerCase();
            if (text.includes(term)) {
                // 高亮匹配文本
                const regex = new RegExp(`(${term})`, 'gi');
                content.innerHTML = content.innerHTML.replace(regex, '<span class="highlight">$1</span>');
                item.style.backgroundColor = '#fef7fb';
            }
        });
    }

    // 移除所有高亮
    function removeHighlights() {
        // 移除表格高亮
        const allTables = document.querySelectorAll('.legal-table');
        allTables.forEach(table => {
            const cells = table.querySelectorAll('td');
            cells.forEach(cell => {
                cell.innerHTML = cell.textContent;
            });
            const rows = table.querySelectorAll('tbody tr');
            rows.forEach(row => {
                row.style.backgroundColor = '';
            });
        });

        // 移除句型高亮
        const sentenceContents = document.querySelectorAll('.sentence-content');
        sentenceContents.forEach(content => {
            const items = content.querySelectorAll('p');
            items.forEach(item => {
                item.innerHTML = item.textContent;
            });
        });
        const sentenceItems = document.querySelectorAll('.sentence-item');
        sentenceItems.forEach(item => {
            item.style.backgroundColor = '';
        });
    }

    // 绑定搜索事件
    searchBtn.addEventListener('click', handleSearch);
    searchInput.addEventListener('keyup', function(e) {
        if (e.key === 'Enter') {
            handleSearch();
        }
    });

    // 3. 表格排序功能
    const tables = document.querySelectorAll('.legal-table');
    tables.forEach(table => {
        const headers = table.querySelectorAll('th');
        headers.forEach((header, index) => {
            header.addEventListener('click', () => {
                sortTable(table, index);
            });
        });
    });

    // 表格排序函数
    function sortTable(table, columnIndex) {
        const tbody = table.querySelector('tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'));
        const isAscending = table.dataset.sortDirection !== 'asc';

        // 排序行
        rows.sort((a, b) => {
            const aText = a.cells[columnIndex].textContent.trim().toLowerCase();
            const bText = b.cells[columnIndex].textContent.trim().toLowerCase();
            return isAscending ? aText.localeCompare(bText) : bText.localeCompare(aText);
        });

        // 重新排列行
        rows.forEach(row => tbody.appendChild(row));

        // 更新排序方向
        table.dataset.sortDirection = isAscending ? 'asc' : 'desc';

        // 更新表头样式
        const headers = table.querySelectorAll('th');
        headers.forEach((header, i) => {
            if (i === columnIndex) {
                header.style.backgroundColor = isAscending ? '#2b6cb0' : '#153e75';
                header.innerHTML = isAscending ? `${header.textContent} <<i class="fas fa-sort-up"></</i>` : `${header.textContent} <<i class="fas fa-sort-down"></</i>`;
            } else {
                header.style.backgroundColor = '#1a365d';
                header.innerHTML = header.textContent.replace(/ <<i class="fas fa-sort-(up|down)"><\/i>/, '');
            }
        });
    }

    // 4. 导航栏滚动效果
    window.addEventListener('scroll', function() {
        const navbar = document.querySelector('.navbar');
        if (window.scrollY > 50) {
            navbar.style.padding = '10px 0';
            navbar.style.boxShadow = '0 4px 15px rgba(0,0,0,0.15)';
        } else {
            navbar.style.padding = '15px 0';
            navbar.style.boxShadow = '0 2px 10px rgba(0,0,0,0.1)';
        }
    });
});