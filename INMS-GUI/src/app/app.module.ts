import { SlideMenuModule } from 'primeng/slidemenu';
import { SharedModule } from 'primeng/components/common/shared';
import { DataTableModule, DataTable } from 'primeng/components/datatable/datatable';
import { TableModule } from 'primeng/components/table/table';
import { ListboxModule } from 'primeng/components/listbox/listbox';
import { CalendarModule } from 'primeng/components/calendar/calendar';
import { DropdownModule } from 'primeng/components/dropdown/dropdown';
import { DialogModule } from 'primeng/components/dialog/dialog';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes, PreloadAllModules } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { CheckboxModule } from 'primeng/components/checkbox/checkbox';
import { OrderListModule } from 'primeng/components/orderlist/orderlist';
import { CommonModule } from '@angular/common';
import { MessagesModule } from 'primeng/components/messages/messages';
import { MessageService } from 'primeng/components/common/messageservice';
import { SidebarModule } from 'primeng/components/sidebar/sidebar';
import { MultiSelectModule } from 'primeng/components/multiselect/multiselect';
import { CardModule } from 'primeng/card';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { RadioButtonModule } from 'primeng/radiobutton';
import { HomeComponent } from './modules/home/home.component';
import { RecipeComponent } from './modules/recipe/recipe.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
const appRoutes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },

  {
    loadChildren: 'app/modules/home/home.module#HomeModule',
    path: 'home'
  },
  {
    loadChildren: 'app/modules/recipe/recipe.module#RecipeModule',
    path: 'recipe'
  }
];
@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, BrowserAnimationsModule, HttpClientModule, RouterModule.forRoot(appRoutes)],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
