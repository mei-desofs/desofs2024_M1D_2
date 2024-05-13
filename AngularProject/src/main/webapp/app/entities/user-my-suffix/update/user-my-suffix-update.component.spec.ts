import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPortfolioMySuffix } from 'app/entities/portfolio-my-suffix/portfolio-my-suffix.model';
import { PortfolioMySuffixService } from 'app/entities/portfolio-my-suffix/service/portfolio-my-suffix.service';
import { UserMySuffixService } from '../service/user-my-suffix.service';
import { IUserMySuffix } from '../user-my-suffix.model';
import { UserMySuffixFormService } from './user-my-suffix-form.service';

import { UserMySuffixUpdateComponent } from './user-my-suffix-update.component';

describe('UserMySuffix Management Update Component', () => {
  let comp: UserMySuffixUpdateComponent;
  let fixture: ComponentFixture<UserMySuffixUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userFormService: UserMySuffixFormService;
  let userService: UserMySuffixService;
  let portfolioService: PortfolioMySuffixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, UserMySuffixUpdateComponent],
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
      .overrideTemplate(UserMySuffixUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserMySuffixUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userFormService = TestBed.inject(UserMySuffixFormService);
    userService = TestBed.inject(UserMySuffixService);
    portfolioService = TestBed.inject(PortfolioMySuffixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PortfolioMySuffix query and add missing value', () => {
      const user: IUserMySuffix = { id: 456 };
      const portfolio: IPortfolioMySuffix = { id: 7312 };
      user.portfolio = portfolio;

      const portfolioCollection: IPortfolioMySuffix[] = [{ id: 11449 }];
      jest.spyOn(portfolioService, 'query').mockReturnValue(of(new HttpResponse({ body: portfolioCollection })));
      const additionalPortfolioMySuffixes = [portfolio];
      const expectedCollection: IPortfolioMySuffix[] = [...additionalPortfolioMySuffixes, ...portfolioCollection];
      jest.spyOn(portfolioService, 'addPortfolioMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ user });
      comp.ngOnInit();

      expect(portfolioService.query).toHaveBeenCalled();
      expect(portfolioService.addPortfolioMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
        portfolioCollection,
        ...additionalPortfolioMySuffixes.map(expect.objectContaining),
      );
      expect(comp.portfoliosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const user: IUserMySuffix = { id: 456 };
      const portfolio: IPortfolioMySuffix = { id: 7817 };
      user.portfolio = portfolio;

      activatedRoute.data = of({ user });
      comp.ngOnInit();

      expect(comp.portfoliosSharedCollection).toContain(portfolio);
      expect(comp.user).toEqual(user);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserMySuffix>>();
      const user = { id: 123 };
      jest.spyOn(userFormService, 'getUserMySuffix').mockReturnValue(user);
      jest.spyOn(userService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ user });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: user }));
      saveSubject.complete();

      // THEN
      expect(userFormService.getUserMySuffix).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userService.update).toHaveBeenCalledWith(expect.objectContaining(user));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserMySuffix>>();
      const user = { id: 123 };
      jest.spyOn(userFormService, 'getUserMySuffix').mockReturnValue({ id: null });
      jest.spyOn(userService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ user: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: user }));
      saveSubject.complete();

      // THEN
      expect(userFormService.getUserMySuffix).toHaveBeenCalled();
      expect(userService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserMySuffix>>();
      const user = { id: 123 };
      jest.spyOn(userService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ user });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userService.update).toHaveBeenCalled();
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
  });
});
