import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'view-purchased-photos',
    loadComponent: () => import('./ViewPurchasedPhotos/viewPurchasedPhotos.component'),
    title: 'View Purchased Photos',
  },
];

export default routes;
