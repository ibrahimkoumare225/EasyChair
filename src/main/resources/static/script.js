function deleteFile(submissionId, filename) {
    fetch(`/submissions/${submissionId}/files/${filename}/delete`, {
        method: 'POST',
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
        .then(response => response.text())
        .then(html => {
            document.getElementById('file-list-container').innerHTML = html;
        })
        .catch(err => console.error("Erreur de suppression : ", err));
}