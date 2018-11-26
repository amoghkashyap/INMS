import { HomeComponent } from './home.component';
import { CommonModule } from '@angular/common';
import { HomeRoutingModule } from './home-routing.module';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CardModule } from 'primeng/card';
import { ListboxModule } from 'primeng/components/listbox/listbox';
import { CheckboxModule } from 'primeng/components/checkbox/checkbox';
import { RadioButtonModule } from 'primeng/radiobutton';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { SlideMenuModule } from 'primeng/slidemenu';
import { OrderListModule } from 'primeng/components/orderlist/orderlist';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/components/dropdown/dropdown';
import { CalendarModule } from 'primeng/components/calendar/calendar';
import { SidebarModule } from 'primeng/components/sidebar/sidebar';
import { DataTableModule } from 'primeng/components/datatable/datatable';
import { MessagesModule } from 'primeng/components/messages/messages';
import { SharedModule } from 'primeng/components/common/shared';
import { TableModule } from 'primeng/components/table/table';
import { DialogModule } from 'primeng/components/dialog/dialog';
import { MultiSelectModule } from 'primeng/components/multiselect/multiselect';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { HomeService } from './home.service';
// import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    HomeRoutingModule,
    CardModule,
    ListboxModule,
    CheckboxModule,
    RadioButtonModule,
    ScrollPanelModule,
    SlideMenuModule,
    OrderListModule,
    FormsModule,
    DropdownModule,
    CalendarModule,
    SidebarModule,
    DataTableModule,
    MessagesModule,
    CheckboxModule,
    CommonModule,
    SharedModule,
    ListboxModule,
    TableModule,
    DialogModule,
    MultiSelectModule
  ],
  providers: [HomeService]
})
export class HomeModule {}
