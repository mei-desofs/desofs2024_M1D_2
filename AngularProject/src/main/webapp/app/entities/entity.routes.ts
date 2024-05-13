import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'cart-my-suffix',
    data: { pageTitle: 'photoStoreApp.cart.home.title' },
    loadChildren: () => import('./cart-my-suffix/cart-my-suffix.routes'),
  },
  {
    path: 'payment-my-suffix',
    data: { pageTitle: 'photoStoreApp.payment.home.title' },
    loadChildren: () => import('./payment-my-suffix/payment-my-suffix.routes'),
  },
  {
    path: 'photo-my-suffix',
    data: { pageTitle: 'photoStoreApp.photo.home.title' },
    loadChildren: () => import('./photo-my-suffix/photo-my-suffix.routes'),
  },
  {
    path: 'portfolio-my-suffix',
    data: { pageTitle: 'photoStoreApp.portfolio.home.title' },
    loadChildren: () => import('./portfolio-my-suffix/portfolio-my-suffix.routes'),
  },
  {
    path: 'receipt-my-suffix',
    data: { pageTitle: 'photoStoreApp.receipt.home.title' },
    loadChildren: () => import('./receipt-my-suffix/receipt-my-suffix.routes'),
  },
  {
    path: 'role-my-suffix',
    data: { pageTitle: 'photoStoreApp.role.home.title' },
    loadChildren: () => import('./role-my-suffix/role-my-suffix.routes'),
  },
  {
    path: 'role-user-my-suffix',
    data: { pageTitle: 'photoStoreApp.roleUser.home.title' },
    loadChildren: () => import('./role-user-my-suffix/role-user-my-suffix.routes'),
  },
  {
    path: 'user-my-suffix',
    data: { pageTitle: 'photoStoreApp.user.home.title' },
    loadChildren: () => import('./user-my-suffix/user-my-suffix.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
