import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPortfolioMySuffix } from 'app/entities/portfolio-my-suffix/portfolio-my-suffix.model';
import { PortfolioMySuffixService } from 'app/entities/portfolio-my-suffix/service/portfolio-my-suffix.service';
import { ICartMySuffix } from 'app/entities/cart-my-suffix/cart-my-suffix.model';
import { CartMySuffixService } from 'app/entities/cart-my-suffix/service/cart-my-suffix.service';
import { IPhotoMySuffix } from '../photo-my-suffix.model';
import { PhotoMySuffixService } from '../service/photo-my-suffix.service';
import { PhotoMySuffixFormService } from './photo-my-suffix-form.service';

import { PhotoMySuffixUpdateComponent } from './photo-my-suffix-update.component';

describe('PhotoMySuffix Management Update Component', () => {
  let comp: PhotoMySuffixUpdateComponent;
  let fixture: ComponentFixture<PhotoMySuffixUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let photoFormService: PhotoMySuffixFormService;
  let photoService: PhotoMySuffixService;
  let portfolioService: PortfolioMySuffixService;
  let cartService: CartMySuffixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PhotoMySuffixUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PhotoMySuffixUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PhotoMySuffixUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    photoFormService = TestBed.inject(PhotoMySuffixFormService);
    photoService = TestBed.inject(PhotoMySuffixService);
    portfolioService = TestBed.inject(PortfolioMySuffixService);
    cartService = TestBed.inject(CartMySuffixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PortfolioMySuffix query and add missing value', () => {
      const photo: IPhotoMySuffix = { id: 456 };
      const portfolio: IPortfolioMySuffix = { id: 21984 };
      photo.portfolio = portfolio;

      const portfolioCollection: IPortfolioMySuffix[] = [{ id: 19343 }];
      jest.spyOn(portfolioService, 'query').mockReturnValue(of(new HttpResponse({ body: portfolioCollection })));
      const additionalPortfolioMySuffixes = [portfolio];
      const expectedCollection: IPortfolioMySuffix[] = [...additionalPortfolioMySuffixes, ...portfolioCollection];
      jest.spyOn(portfolioService, 'addPortfolioMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ photo });
      comp.ngOnInit();

      expect(portfolioService.query).toHaveBeenCalled();
      expect(portfolioService.addPortfolioMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
        portfolioCollection,
        ...additionalPortfolioMySuffixes.map(expect.objectContaining),
      );
      expect(comp.portfoliosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CartMySuffix query and add missing value', () => {
      const photo: IPhotoMySuffix = { id: 456 };
      const cart: ICartMySuffix = { id: 14319 };
      photo.cart = cart;

      const cartCollection: ICartMySuffix[] = [{ id: 5585 }];
      jest.spyOn(cartService, 'query').mockReturnValue(of(new HttpResponse({ body: cartCollection })));
      const additionalCartMySuffixes = [cart];
      const expectedCollection: ICartMySuffix[] = [...additionalCartMySuffixes, ...cartCollection];
      jest.spyOn(cartService, 'addCartMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ photo });
      comp.ngOnInit();

      expect(cartService.query).toHaveBeenCalled();
      expect(cartService.addCartMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
        cartCollection,
        ...additionalCartMySuffixes.map(expect.objectContaining),
      );
      expect(comp.cartsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const photo: IPhotoMySuffix = { id: 456 };
      const portfolio: IPortfolioMySuffix = { id: 6182 };
      photo.portfolio = portfolio;
      const cart: ICartMySuffix = { id: 1361 };
      photo.cart = cart;

      activatedRoute.data = of({ photo });
      comp.ngOnInit();

      expect(comp.portfoliosSharedCollection).toContain(portfolio);
      expect(comp.cartsSharedCollection).toContain(cart);
      expect(comp.photo).toEqual(photo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhotoMySuffix>>();
      const photo = { id: 123 };
      jest.spyOn(photoFormService, 'getPhotoMySuffix').mockReturnValue(photo);
      jest.spyOn(photoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ photo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: photo }));
      saveSubject.complete();

      // THEN
      expect(photoFormService.getPhotoMySuffix).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(photoService.update).toHaveBeenCalledWith(expect.objectContaining(photo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhotoMySuffix>>();
      const photo = { id: 123 };
      jest.spyOn(photoFormService, 'getPhotoMySuffix').mockReturnValue({ id: null });
      jest.spyOn(photoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ photo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: photo }));
      saveSubject.complete();

      // THEN
      expect(photoFormService.getPhotoMySuffix).toHaveBeenCalled();
      expect(photoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhotoMySuffix>>();
      const photo = { id: 123 };
      jest.spyOn(photoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ photo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(photoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePortfolioMySuffix', () => {
      it('Should forward to portfolioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(portfolioService, 'comparePortfolioMySuffix');
        comp.comparePortfolioMySuffix(entity, entity2);
        expect(portfolioService.comparePortfolioMySuffix).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCartMySuffix', () => {
      it('Should forward to cartService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cartService, 'compareCartMySuffix');
        comp.compareCartMySuffix(entity, entity2);
        expect(cartService.compareCartMySuffix).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
