import {AlertTriangle, Trash2, X, Loader2} from 'lucide-react';

export default function ConfirmModal({
                                         isOpen,
                                         onClose,
                                         onConfirm,
                                         title = 'Confirm Action',
                                         message = 'Are you sure you want to proceed?',
                                         confirmText = 'Confirm',
                                         cancelText = 'Cancel',
                                         variant = 'danger', // 'danger' | 'warning' | 'primary'
                                         loading = false,
                                     }) {
    if (!isOpen) return null;

    const variantStyles = {
        danger: {
            icon: Trash2,
            iconBg: 'bg-red-50 text-red-600 border-red-100',
            button: 'bg-red-600 hover:bg-red-700 text-white',
        },
        warning: {
            icon: AlertTriangle,
            iconBg: 'bg-amber-50 text-amber-600 border-amber-100',
            button: 'bg-amber-600 hover:bg-amber-700 text-white',
        },
        primary: {
            icon: AlertTriangle,
            iconBg: 'bg-primary-50 text-primary-600 border-primary-100',
            button: 'bg-primary-600 hover:bg-primary-700 text-white',
        },
    };

    const style = variantStyles[variant] || variantStyles.danger;
    const Icon = style.icon;

    return (
        <div
            className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-xs transition-opacity"
            onClick={onClose}
        >
            <div
                className="bg-white rounded-2xl border border-slate-200 shadow-2xl max-w-sm w-full p-6 relative overflow-hidden transition-all"
                onClick={(e) => e.stopPropagation()}
            >
                {/* Close X Button */}
                <button
                    type="button"
                    onClick={onClose}
                    disabled={loading}
                    className="absolute top-4 right-4 text-slate-400 hover:text-slate-600 transition p-1 rounded-lg hover:bg-slate-100"
                >
                    <X className="w-4 h-4"/>
                </button>

                <div className="flex flex-col items-center text-center">
                    {/* Icon Badge */}
                    <div
                        className={`w-12 h-12 rounded-2xl border flex items-center justify-center mb-4 ${style.iconBg} shadow-xs`}>
                        <Icon className="w-6 h-6"/>
                    </div>

                    {/* Title & Message */}
                    <h3 className="text-base font-bold text-slate-900 mb-1.5">{title}</h3>
                    <p className="text-xs text-slate-500 leading-relaxed max-w-xs mb-6">
                        {message}
                    </p>

                    {/* Action Buttons */}
                    <div className="flex items-center gap-3 w-full">
                        <button
                            type="button"
                            onClick={onClose}
                            disabled={loading}
                            className="flex-1 px-4 py-2.5 text-xs font-semibold text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-xl transition"
                        >
                            {cancelText}
                        </button>
                        <button
                            type="button"
                            onClick={onConfirm}
                            disabled={loading}
                            className={`flex-1 px-4 py-2.5 text-xs font-semibold rounded-xl shadow-sm transition flex items-center justify-center gap-2 ${style.button}`}
                        >
                            {loading && <Loader2 className="w-3.5 h-3.5 animate-spin"/>}
                            <span>{confirmText}</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}