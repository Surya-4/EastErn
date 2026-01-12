import { Component, HostListener, ElementRef } from '@angular/core';
import { UserService, UserData } from '../../services/user-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { P } from '@angular/cdk/keycodes';
import { Toast } from '../../services/toast';


@Component({
  standalone: true,
  selector: 'app-profile',
  imports: [FormsModule, CommonModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})

export class Profile {

  user: UserData = {
    address: {}
  };

  editMode: { [key in keyof UserData]?: boolean } = {};
  originalUser: UserData = {
    address: {}
  };
  isDirty = false;
  fields: (keyof UserData)[] = ['userName', 'firstName', 'lastName', 'email', 'mobile', 'address'];

  constructor(private userService: UserService, private elRef: ElementRef,private toast:Toast) { }

  ngOnInit(): void {
    this.userService.userData$.subscribe(data => {
      this.user = { ...data };
      this.originalUser = { ...data };

      if (!this.user.address) this.user.address = {};
      if (!this.originalUser.address) this.originalUser.address = {};
    })
  }

  enableEdit(field: keyof UserData, event: Event) {
    event?.stopPropagation();
    for(let key of this.fields){
      this.editMode[key]=false;
    }
    this.editMode[field] = true;
  }

  onValueChange() {
    this.isDirty = JSON.stringify(this.user) !== JSON.stringify(this.originalUser);
  }

  updateProfile() {
    this.userService.editUser(this.user).subscribe(() => {
      this.originalUser = { ...this.user };
      this.isDirty = false;
      this.editMode = {};
      this.toast.show({
        text:'Profile succesfully updated',
        type:'success'
      })
    });
  }

  exitEditMode(field?: keyof UserData) {
    if (field) {
      this.editMode[field] = false;
    } else {
      for (let key of this.fields) {
        this.editMode[key] = false;
      }
    }
  }

  cancelEdit(){
    for(let field of this.fields){
      this.editMode[field]=false;
    }
    this.isDirty=false;
    this.user=this.originalUser;
  }

  addAddress() {
    if (!this.user.address) this.user.address = {};

    let newKey = 'address_' + (Object.keys(this.user.address).length + 1);

    this.user.address[newKey] = '';
    this.onValueChange();
  }

  removeAddress(key: string) {
    delete this.user.address![key];
    this.onValueChange();
  }

  trackByAddr(index: number, item: { key: string; value: string }) {
    return item.key;
  }

  renameAddressKey(oldKey: string, newKey: string) {
    if (!newKey || oldKey === newKey) return;

    // prevent overwriting existing key
    if (this.user.address?.[newKey]) {
      alert('Address name already exists');
      return;
    }

    this.user.address![newKey] = this.user.address![oldKey];
    delete this.user.address![oldKey];

    this.onValueChange();
  }


  @HostListener('document:click', ['$event'])
  onClickOutside(event: Event) {
    // Check if click is inside this component
    for (let field of this.fields) {
      const fieldEl = this.elRef.nativeElement.querySelector(`#field-${field}`);
      if (this.editMode[field] && fieldEl && !fieldEl.contains(event.target)) {
        this.exitEditMode(field);
      }
      const addressEl = this.elRef.nativeElement.querySelector('#address-section');
      if (this.editMode['address'] && addressEl && !addressEl.contains(event.target)) {
        this.exitEditMode('address');
      }
    }
  }
}