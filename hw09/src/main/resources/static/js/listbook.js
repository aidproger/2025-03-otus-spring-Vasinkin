function confirmDelete(element) {
    const question = element.getAttribute('data-question');
    return confirm(question);
}