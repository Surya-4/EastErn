import { Component } from '@angular/core';
import { Toast, ToastMessage } from '../../services/toast';
import { CommonModule, NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-toast-component',
  imports: [CommonModule,NgIf,NgFor],
  templateUrl: './toast-component.html',
  styleUrl: './toast-component.css',
})
export class ToastComponent {

  constructor(public toastService: Toast) {}

 handleAction(msg: ToastMessage, type: 'ok' | 'cancel') {
  if (type === 'ok' && msg.action?.callback) {
    msg.action.callback();
  }
  if (type === 'cancel' && msg.cancelAction?.callback) {
    msg.cancelAction.callback();
  }
  this.toastService.remove(msg);
}


}
