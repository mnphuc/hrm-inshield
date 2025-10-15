(() => {
    const body = document.body;

    function closeModal(modal) {
        if (!modal) {
            return;
        }
        modal.classList.remove('show');
        body.classList.remove('modal-open');
    }

    function toDatasetKey(field) {
        const normalised = field.replace(/([A-Z])/g, '-$1').toLowerCase();
        const parts = normalised.split('-').filter(Boolean);
        return 'value' + parts.map((part) => part.charAt(0).toUpperCase() + part.slice(1)).join('');
    }

    function setFieldValue(field, value) {
        if (field.type === 'checkbox') {
            field.checked = value === 'true' || value === true;
        } else {
            field.value = value ?? '';
        }
    }

    function clearFormFields(form) {
        form.querySelectorAll('[data-field]').forEach((element) => {
            setFieldValue(element, '');
        });
    }

    function populateFormFields(form, dataset, mode) {
        form.querySelectorAll('[data-field]').forEach((element) => {
            const fieldName = element.dataset.field;
            if (!fieldName) {
                return;
            }
            if (mode === 'edit') {
                const key = toDatasetKey(fieldName);
                const value = dataset ? dataset[key] : undefined;
                setFieldValue(element, value);
            } else {
                setFieldValue(element, '');
            }
        });
    }

    function deriveAction(mode, modal, dataset) {
        const createUrl = modal.dataset.createUrl;
        if (mode === 'edit' && dataset) {
            if (dataset.updateUrl) {
                return dataset.updateUrl;
            }

            const updateTemplate = modal.dataset.updateTemplate;
            if (updateTemplate && dataset.recordId) {
                return updateTemplate.replace('__ID__', dataset.recordId);
            }
        }

        return createUrl || '';
    }

    function openModal(modal, options = {}) {
        if (!modal) {
            return;
        }
        const trigger = options.trigger || null;
        const skipPopulate = options.skipPopulate === true;
        const forcedMode = options.mode;
        const dataset = trigger ? trigger.dataset : {};
        const mode = forcedMode || dataset.mode || modal.dataset.mode || 'create';
        const form = modal.querySelector('form');
        const modalDataset = Object.assign({}, dataset);

        if (trigger && trigger.dataset.recordId) {
            modalDataset.recordId = trigger.dataset.recordId;
        } else if (modal.dataset.recordId) {
            modalDataset.recordId = modal.dataset.recordId;
        }

        if (form) {
            form.action = deriveAction(mode, modal, modalDataset);
            if (!skipPopulate) {
                if (mode === 'edit') {
                    populateFormFields(form, dataset, 'edit');
                } else {
                    clearFormFields(form);
                }
            }
        }

        const title = modal.querySelector('[data-modal-title]');
        if (title) {
            const createTitle = modal.dataset.createTitle || title.dataset.createTitle || title.textContent || 'Them moi';
            const editTitle = modal.dataset.editTitle || title.dataset.editTitle || 'Cap nhat';
            title.textContent = mode === 'edit' ? editTitle : createTitle;
        }

        const submit = modal.querySelector('[data-modal-submit]');
        if (submit) {
            const createText = modal.dataset.createText || submit.dataset.createText || submit.textContent || 'Luu';
            const editText = modal.dataset.editText || submit.dataset.editText || 'Cap nhat';
            submit.textContent = mode === 'edit' ? editText : createText;
        }

        modal.dataset.mode = mode;
        if (modalDataset.recordId) {
            modal.dataset.recordId = modalDataset.recordId;
        }

        modal.classList.add('show');
        body.classList.add('modal-open');
        modal.focus();
    }

    function initialiseModals() {
        const modals = document.querySelectorAll('[data-modal]');
        modals.forEach((modal) => {
            modal.querySelectorAll('[data-close-modal]').forEach((button) => {
                button.addEventListener('click', () => closeModal(modal));
            });

            modal.addEventListener('click', (event) => {
                if (event.target === modal) {
                    closeModal(modal);
                }
            });
        });

        document.addEventListener('keydown', (event) => {
            if (event.key === 'Escape') {
                const openModalElement = document.querySelector('[data-modal].show');
                if (openModalElement) {
                    closeModal(openModalElement);
                }
            }
        });

        document.querySelectorAll('[data-open-modal]').forEach((trigger) => {
            trigger.addEventListener('click', () => {
                const modalId = trigger.dataset.openModal;
                const modal = document.getElementById(modalId);
                if (!modal) {
                    return;
                }
                openModal(modal, { trigger });
            });
        });

        modals.forEach((modal) => {
            if (modal.dataset.open === 'true') {
                openModal(modal, { mode: modal.dataset.mode, skipPopulate: true });
            }
        });
    }

    function initialiseConfirms() {
        document.querySelectorAll('form[data-confirm-message]').forEach((form) => {
            form.addEventListener('submit', (event) => {
                const message = form.dataset.confirmMessage || 'Ban co chac chan?';
                if (!window.confirm(message)) {
                    event.preventDefault();
                }
            });
        });
    }

    document.addEventListener('DOMContentLoaded', () => {
        initialiseModals();
        initialiseConfirms();
    });
})();

