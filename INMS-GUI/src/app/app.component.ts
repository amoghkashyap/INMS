import { Component } from '@angular/core';
import { SlideMenuModule } from 'primeng/slidemenu';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'app';
  items: MenuItem[];
  public selectedCities;
  public displayDialog = false;
  cars = [
    { recipe: 'Onion', year: 2012, color: 'Orange', vin: 'dsad231ff' },
    { recipe: 'Tomato', year: 2011, color: 'Black', vin: 'gwregre345' },
    { recipe: 'Egg', year: 2005, color: 'Gray', vin: 'h354htr' },
    { recipe: 'Carrot', year: 2003, color: 'Blue', vin: 'j6w54qgh' },
    { recipe: 'Milk', year: 1995, color: 'Orange', vin: 'hrtwy34' },
    { recipe: 'Capsicum', year: 2005, color: 'Black', vin: 'jejtyj' }
  ];
  constructor() {}

  ngOnInit() {
    this.items = [
      {
        label: 'File',
        items: [
          {
            label: 'New',
            icon: 'pi pi-fw pi-plus',
            items: [{ label: 'Project' }, { label: 'Other' }]
          },
          { label: 'Open' },
          { label: 'Quit' }
        ]
      },
      {
        label: 'Edit',
        icon: 'pi pi-fw pi-pencil',
        items: [{ label: 'Delete', icon: 'pi pi-fw pi-trash' }, { label: 'Refresh', icon: 'pi pi-fw pi-refresh' }]
      }
    ];
  }
  onChange(): void {
    console.log(this.selectedCities);
  }

  public showDialog(): void {
    this.displayDialog = true;
  }
}
