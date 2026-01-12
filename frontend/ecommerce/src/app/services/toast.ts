import { Injectable, signal } from '@angular/core';

export interface ToastMessage {
  text: string;
  type: 'success' | 'error' | 'info';
  duration?: number; // in ms, optional
  action?: { text: string; callback: () => void };
  cancelAction?: { text: string; callback: () => void }; 
}

@Injectable({
  providedIn: 'root',
})
export class Toast {

  private _messages = signal<ToastMessage[]>([]);
  messages = this._messages.asReadonly();

  private DEFAULT_DURATION = 3000; 

  show(message: ToastMessage) {
    const newMessage = { ...message };

    if (!newMessage.duration) {
      newMessage.duration = this.DEFAULT_DURATION;
    }

    this._messages.set([...this._messages(), newMessage]);

    if (!newMessage.action && !newMessage.cancelAction) {
      setTimeout(() => {
        this.remove(newMessage);
      }, newMessage.duration);
    }
  }

  remove(message: ToastMessage) {
    this._messages.set(this._messages().filter(m => m !== message));
  }
}
