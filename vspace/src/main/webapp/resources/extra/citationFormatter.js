var VSpaceCitation = (function() {

    /**
     * Maps Virtual Spaces reference types to CSL-JSON types
     */
    var typeMapping = {
        'Book': 'book',
        'Journal Article': 'article-journal',
        'Conference Paper': 'paper-conference',
        'Thesis': 'thesis',
        'Report': 'report',
        'Website': 'webpage',
        'Patent': 'patent',
        'Other': 'document'
    };

    /**
     * Parses author string from "LastName, FirstName; LastName, FirstName" format
     * to CSL-JSON author array format
     * @param {string} authorString - Author string in VSpace format
     * @returns {Array} Array of author objects with family and given names
     */
    function parseAuthors(authorString) {
        if (!authorString || authorString.trim() === '') {
            return [];
        }

        var authors = [];
        var authorParts = authorString.split(';');

        for (var i = 0; i < authorParts.length; i++) {
            var author = authorParts[i].trim();
            if (author === '') continue;

            var nameParts = author.split(',');
            if (nameParts.length >= 2) {
                authors.push({
                    family: nameParts[0].trim(),
                    given: nameParts[1].trim()
                });
            } else {
                authors.push({
                    literal: author
                });
            }
        }

        return authors;
    }

    /**
     * Parses editors string using same format as authors
     * @param {string} editorsString - Editors string
     * @returns {Array} Array of editor objects
     */
    function parseEditors(editorsString) {
        return parseAuthors(editorsString);
    }

    /**
     * Converts Virtual Spaces Reference object to CSL-JSON format
     * @param {Object} reference - Reference object with VSpace fields
     * @returns {Object} CSL-JSON formatted object
     */
    function convertToCslJson(reference) {
        var cslItem = {
            id: reference.id || 'ref-' + Date.now(),
            type: typeMapping[reference.type] || 'document'
        };

        if (reference.title) {
            cslItem.title = reference.title;
        }

        if (reference.author) {
            cslItem.author = parseAuthors(reference.author);
        }

        if (reference.year) {
            var year = parseInt(reference.year, 10);
            if (!isNaN(year)) {
                cslItem.issued = {
                    'date-parts': [[year]]
                };
            }
        }

        if (reference.journal) {
            cslItem['container-title'] = reference.journal;
        }

        if (reference.url) {
            cslItem.URL = reference.url;
        }

        if (reference.volume) {
            cslItem.volume = reference.volume;
        }

        if (reference.issue) {
            cslItem.issue = reference.issue;
        }

        if (reference.pages) {
            cslItem.page = reference.pages;
        }

        if (reference.editors) {
            cslItem.editor = parseEditors(reference.editors);
        }

        if (reference.note) {
            cslItem.note = reference.note;
        }

        return cslItem;
    }

    /**
     * Formats a single reference using citation.js
     * @param {Object} reference - Reference object
     * @param {string} style - Citation style ('apa', 'chicago', 'mla')
     * @returns {string} Formatted HTML string
     */
    function formatReference(reference, style) {
        try {
            var Cite = require('citation-js');
            var cslData = convertToCslJson(reference);
            var cite = new Cite(cslData);

            var templateMap = {
                'apa': 'apa',
                'chicago': 'chicago-fullnote-bibliography',
                'mla': 'modern-language-association'
            };

            var template = templateMap[style] || 'apa';

            return cite.format('bibliography', {
                format: 'html',
                template: template,
                lang: 'en-US'
            });
        } catch (e) {
            console.error('Citation formatting error:', e);
            // Fallback to basic formatting
            return formatFallback(reference);
        }
    }

    /**
     * Fallback formatting when citation.js fails
     * @param {Object} reference - Reference object
     * @returns {string} Basic formatted HTML
     */
    function formatFallback(reference) {
        var parts = [];
        if (reference.author) parts.push(reference.author);
        if (reference.year) parts.push('(' + reference.year + ')');
        if (reference.title) parts.push('<em>' + escapeHtml(reference.title) + '</em>');
        if (reference.journal) parts.push(escapeHtml(reference.journal));
        if (reference.volume) parts.push(reference.volume);
        if (reference.issue) parts.push('(' + reference.issue + ')');
        if (reference.pages) parts.push(reference.pages);
        if (reference.url) parts.push('<a href="' + escapeHtml(reference.url) + '" target="_blank">' + escapeHtml(reference.url) + '</a>');

        return '<p>' + parts.join('. ') + '.</p>';
    }

    /**
     * Escapes HTML special characters
     * @param {string} text - Text to escape
     * @returns {string} Escaped text
     */
    function escapeHtml(text) {
        if (!text) return '';
        var div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    /**
     * Formats all references in a BiblioBlock
     * @param {Array} references - Array of reference objects
     * @param {string} style - Citation style
     * @returns {string} Formatted HTML for all references
     */
    function formatBibliography(references, style) {
        var html = '';
        for (var i = 0; i < references.length; i++) {
            html += '<div class="formatted-reference">' +
                    formatReference(references[i], style) +
                    '</div>';
        }
        return html;
    }

    return {
        convertToCslJson: convertToCslJson,
        formatReference: formatReference,
        formatBibliography: formatBibliography,
        parseAuthors: parseAuthors
    };

})();
