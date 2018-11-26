import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { HomeService } from './home.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  title = 'app';
  items: MenuItem[];
  public selectedCities;
  public selectedIngredients = [];
  public displayAddIng = false;
  public selectedHealthLabel = [];
  public displayDialog = false;
  public showRecipe = false;
  public recipeData = [];
  public ingredientsFromServer = [];
  public addedIng = '';
  public selectedRecipe = {
    image: '',
    value: '',
    ingredientLines: [],
    calorieCount: '',
    preparationTime: ''
  };
  public healthLabels = [
    { label: 'balanced' },
    { label: 'high-protein' },
    { label: 'low-fat' },
    { label: 'low-carb' },
    { label: 'alcohol-free' },
    { label: 'vegan' },
    { label: 'vegetarian' },
    { label: 'sugar-conscious' },
    { label: 'tree-nut-free' },
    { label: 'peanut-free' }
  ];

  public ingredients = [];

  constructor(private http: HttpClient, public homeService: HomeService) {}

  ngOnInit() {
    this.getIngredients();
  }
  public getRecipeFromServer(): void {
    const ingredient = [];
    const healthLabels = [];
    if (this.selectedIngredients.length === 0) {
      for (let i = 0; i < this.ingredients.length; i++) {
        ingredient.push(this.ingredients[i].recipe);
      }
    } else {
      for (let i = 0; i < this.selectedIngredients.length; i++) {
        ingredient.push(this.selectedIngredients[i].recipe);
      }
    }

    for (let i = 0; i < this.selectedHealthLabel.length; i++) {
      healthLabels.push(this.selectedHealthLabel[i].label);
    }
    // if (healthLabels.length === 0) {
    //   healthLabels.push('vegan');
    // }

    const requestData = { container_id: 'inms', ingredients: ingredient, health_labels: healthLabels };
    this.homeService.getRecipe(requestData).subscribe(res => {
      this.recipeData = [];
      console.log(res.recipes);
      for (let i = 0; i < res.recipes.length; i++) {
        const value = [];
        for (let j = 0; j < res.recipes[i].ingredients.length; j++) {
          value.push(res.recipes[i].ingredients[j]);
        }
        const obj = {
          image: res.recipes[i].image_url,
          value: res.recipes[i].recipe_name,
          ingredientLines: value,
          calorieCount: res.recipes[i].calorie_count,
          preparationTime: res.recipes[i].preparation_time,
          preparationUrl: res.recipes[i].preparation_url
        };
        this.recipeData.push(obj);
        this.selectedRecipe = this.recipeData[0];
      }
    });
  }
  public getIngredients(): void {
    this.homeService.getIngredients().subscribe(res => {
      this.ingredients = [];
      for (let i = 0; i < res.ingredients.length; i++) {
        this.ingredients.push({ recipe: res.ingredients[i] });
      }
      // this.ingredientsFromServer = this.ingredients;
    });
  }
  public onChange(): void {}
  public onChangeOfRecipe(item): void {
    this.selectedRecipe = item;
  }

  public home(): void {
    this.showRecipe = false;
  }
  public getRecipe(): void {
    console.log('get');
    this.showRecipe = true;
    this.getRecipeFromServer();
  }
  public saveHealthLabel(): void {
    this.displayDialog = false;
    this.getRecipeFromServer();
  }

  public showDialog(): void {
    this.displayDialog = true;
  }

  public submitIng(): void {
    this.getRecipeFromServer();
  }

  public addIng(): void {
    this.displayAddIng = true;
  }

  public saveAddedIng(): void {
    this.displayAddIng = false;

    const ingredients = [];
    for (let i = 0; i < this.ingredients.length; i++) {
      ingredients.push(this.ingredients[i].recipe);
    }
    ingredients.push(this.addedIng);
    // this.ingredients = [...this.ingredients, { recipe: this.addedIng }];

    const params = {
      container_id: 'inms',
      ingredients: ingredients
    };
    console.log(params);

    this.homeService.addIng(params).subscribe(res => {
      console.log('adding', res);
      this.addedIng = '';
      if (res.status.status_code === 'SUCCESS') {
        this.getIngredients();
      }
    });
  }
}
